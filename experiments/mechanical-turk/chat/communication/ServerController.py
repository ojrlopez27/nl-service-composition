

"""
Majordomo Protocol Worker API, Python version
Implements the MDP/Worker spec at http:#rfc.zeromq.org/spec:7.
"""

import time

import zmq

# MajorDomo protocol constants:
from utils import Constants


class ServerController(object):
    """Majordomo Protocol Worker API, Python version

    Implements the MDP/Worker spec at http:#rfc.zeromq.org/spec:7.
    """

    HEARTBEAT_LIVENESS = 3 # 3-5 is reasonable
    broker = None
    ctx = None
    service = None

    worker = None # Socket to broker
    heartbeat_at = 0 # When to send HEARTBEAT (relative to time.time(), so in seconds)
    liveness = 0 # How many attempts left
    heartbeat = 2500 # Heartbeat delay, msecs
    reconnect = 2500 # Reconnect delay, msecs

    # Internal state
    expect_reply = False # False only at start

    timeout = 2500 # poller timeout
    verbose = False # Print activity to stdout

    # Return address, if any
    reply_to = None

    def __init__(self, broker, service, verbose=False):
        self.broker = broker
        self.service = bytes(service.encode("UTF-8"))
        self.verbose = verbose
        self.ctx = zmq.Context()
        self.poller = zmq.Poller()
        self.reconnect_to_broker()

    def reconnect_to_broker(self):
        """Connect or reconnect to broker"""
        if self.worker:
            self.poller.unregister(self.worker)
            self.worker.close()
        self.worker = self.ctx.socket(zmq.DEALER)
        self.worker.linger = 0
        self.worker.connect(self.broker)
        self.poller.register(self.worker, zmq.POLLIN)
        if self.verbose:
            print("I: connecting to broker at %s", self.broker)

        # Register service with broker
        self.send_to_broker(Constants.S_READY, self.service, [])

        # If liveness hits zero, queue is considered disconnected
        self.liveness = self.HEARTBEAT_LIVENESS
        self.heartbeat_at = time.time() + 1e-3 * self.heartbeat


    def send_to_broker(self, command, option=None, msg=None):
        """Send message to broker.
        If no msg is provided, creates one internally
        """
        if msg is None:
            msg = []
        elif not isinstance(msg, list):
            msg = [msg]
        if option:
            msg = [option] + msg
        msg = [b'', Constants.S_WORKER, command] + msg
        #if self.verbose:
        #    print("I: sending %s to broker", command)
        self.worker.send_multipart(msg)


    def recv(self):
        """Send reply, if any, to broker and wait for next request."""
        self.expect_reply = True

        while True:
            # Poll socket for a reply, with timeout
            try:
                items = self.poller.poll(self.timeout)
            except KeyboardInterrupt:
                break # Interrupted

            if items:
                msg = self.worker.recv_multipart()
                #if self.verbose:
                #    print("I: received message from broker: ")

                self.liveness = self.HEARTBEAT_LIVENESS
                # Don't try to handle errors, just assert noisily
                assert len(msg) >= 3

                empty = msg.pop(0)
                assert empty == b''

                header = msg.pop(0)
                assert header == Constants.S_WORKER

                command = msg.pop(0)
                if command == Constants.S_REQUEST:
                    # We should pop and save as many addresses as there are
                    # up to a null part, but for now, just save one
                    reply_to = msg.pop(0)
                    # pop empty
                    empty = msg.pop(0)
                    assert empty == b''
		    
		    print("I: received message: %s", msg[0] )
                    return [reply_to, b'', msg[0]] # We have a request to process
                elif command == Constants.S_HEARTBEAT:
                    # Do nothing for heartbeats
                    pass
                elif command == Constants.S_DISCONNECT:
                    self.reconnect_to_broker()
                else :
                    print("E: invalid input message: ")

            else:
                self.liveness -= 1
                if self.liveness == 0:
                    if self.verbose:
                        print("W: disconnected from broker - retrying")
                    try:
                        time.sleep(1e-3*self.reconnect)
                    except KeyboardInterrupt:
                        break
                    self.reconnect_to_broker()

            # Send HEARTBEAT if it's time
            if time.time() > self.heartbeat_at:
                self.send_to_broker(Constants.S_HEARTBEAT)
                self.heartbeat_at = time.time() + 1e-3*self.heartbeat

        print("W: interrupt received, killing worker")
        return None


    def send(self, reply):
        if reply is not None and isinstance(reply, str):
            reply = bytes(reply, "UTF-8")
        if reply is not None:
            assert reply[0] is not None  #reply_to
            self.send_to_broker(Constants.S_REPLY, msg=reply)


    def destroy(self):
        # context.destroy depends on pyzmq >= 2.1.10
        self.ctx.destroy(0)
