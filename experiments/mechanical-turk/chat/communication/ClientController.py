import logging

import zmq

import utils.Constants as Constants
from model.SessionMessage import SessionMessage
from model.UserUtterance import UserUtterance

class ClientController(object):
    """Majordomo Protocol Client API, Python version.

      Implements the MDP/Worker spec at http:#rfc.zeromq.org/spec:7.
    """
    broker = None
    ctx = None
    client = None
    poller = None
    timeout = 2500
    retries = 3
    verbose = True
    sessionId = None

    def __init__(self, broker, sessionId, verbose=False):
        self.broker = broker
        self.verbose = verbose
        self.ctx = zmq.Context()
        self.poller = zmq.Poller()
        self.sessionId = sessionId
        logging.basicConfig(format="%(asctime)s %(message)s", datefmt="%Y-%m-%d %H:%M:%S",
                level=logging.INFO)
        self.reconnect_to_broker()

    def reconnect_to_broker(self):
        """Connect or reconnect to broker"""
        if self.client:
            self.poller.unregister(self.client)
            self.client.close()
        self.client = self.ctx.socket(zmq.REQ)
        self.client.linger = 0
        self.client.connect(self.broker)
        self.poller.register(self.client, zmq.POLLIN)
        if self.verbose:
            logging.info("I: connecting to broker at %s", self.broker)

     
    def sendMsg(self, request):
        sessionMessage = SessionMessage(Constants.MSG_SEND_USER_UTTERANCE, self.sessionId, "", request.toString(), "").toString()
        self.send(self.sessionId, sessionMessage)        

    def sendInitMsg(self):
        sessionMessage = SessionMessage(Constants.MSG_CHECK_USER_ID, self.sessionId, "", "", "").toString()
        return self.send(self.sessionId, sessionMessage)


    def send(self, service, request):
        print("Eneter into send")
        print(request)
        service = bytes(service.encode("UTF-8"))
        request = bytes(request.encode("UTF-8"))

        """Send request to broker and get reply by hook or crook.

        Takes ownership of request message and destroys it when sent.
        Returns the reply message or None if there was no reply.
        """
        if not isinstance(request, list):
            request = [request]
        request = [Constants.C_CLIENT, service] + request
        if self.verbose:
            logging.warn("I: send request to '%s' service: ", service)
        reply = None

        retries = self.retries
        while retries > 0:
            self.client.send_multipart(request)
            try:
                items = self.poller.poll(self.timeout)
            except KeyboardInterrupt:
                break # interrupted

            if items:
                msg = self.client.recv_multipart()
                if self.verbose:
                    logging.info("I: received reply: %s",msg)

                # Don't try to handle errors, just assert noisily
                assert len(msg) >= 3

                header = msg.pop(0)
                # assert Constants.C_CLIENT == header

                reply_service = msg.pop(0)
                # assert service == reply_service

                reply = msg
                    
                break
            else:
                if retries:
                    logging.warn("W: no reply, reconnecing")
                    self.reconnect_to_broker()
                else:
                    logging.warn("W: permanent error, abandoning")
                    break
                retries -= 1

        return reply


    def destroy(self):
        self.context.destroy()


    def connect(self):
        reply = self.send("session-manager", SessionMessage("REQUEST_CONNECT", self.sessionId, "", "", "").toString())        
        if "SESSION_INITIATED" in str(reply) or "SESSION_RECONNECTED" in str(reply):        
            return self.sendInitMsg()

