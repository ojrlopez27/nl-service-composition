#!/usr/bin/env python3
#
# Copyright 2009 Facebook
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License. You may obtain
# a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.

import asyncio
import tornado.escape
import tornado.ioloop
import tornado.locks
import tornado.web
import os.path
import uuid
import time
import threading
from communication.ClientController import ClientController
from utils.config import config
from utils import Constants
from tornado.options import define, options, parse_command_line

# Global variables
define("port", default=config.python_server_port, help="run on the given port", type=int)
define("debug", default=True, help="run in debug mode")
java_server = config.java_server
verbose = False

class MessageBuffer(object):
    def __init__(self):
        # cond is notified whenever the message cache is updated
        self.cond = tornado.locks.Condition()
        self.cache = []
        self.cache_size = 200
        self.should_process_input_from_user = False
        self.session_id = None
        self.is_new_session = True
        self.user_id_validation = None
        self.manager = ClientController(java_server, "manager", verbose)            
        self.manager.connect()
        self.client = None

    def get_messages_since(self, cursor):
        """Returns a list of messages newer than the given cursor.

        ``cursor`` should be the ``id`` of the last message received.
        """
        results = []
        for msg in reversed(self.cache):
            if msg["id"] == cursor:
                break
            # print(msg)
            results.append(msg)        
        results.reverse()
        return results

    def add_message(self, message):
        self.cache.append(message)
        if len(self.cache) > self.cache_size:
            self.cache = self.cache[-self.cache_size:]
        self.cond.notify_all()

# global (but it has to be defined here)
global_message_buffer = MessageBuffer()

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("index.html", messages=global_message_buffer.cache)


class MessageNewHandler(tornado.web.RequestHandler):

    """Post a new message to the chat room."""
    def post(self):
        body = self.get_argument("body")
        if global_message_buffer.is_new_session:
            global_message_buffer.session_id = body
            # connecting to Java code...            
            response = global_message_buffer.manager.checkUser(body)               
            response = response[0].decode("utf-8")         
            if response.startswith('Thanks!'):
                global_message_buffer.client = ClientController(java_server, body, verbose)            
                global_message_buffer.client.connect()                        
                global_message_buffer.session_id = body
                global_message_buffer.is_new_session = False
            global_message_buffer.user_id_validation = "[IPA]: " + response
            body = "[You]: " + body
        else:    
            global_message_buffer.client.sendUserAction(body)

        message = {
           "id": str(uuid.uuid4()),
           "body": body,
        }

        # render_string() returns a byte string, which is not supported
        # in json, so we must convert it to a character string.
        message["html"] = tornado.escape.to_unicode(
        self.render_string("message.html", message=message)) 
        
        if self.get_argument("next", None):        
            self.redirect(self.get_argument("next"))
        else:        
            self.write(message)
        global_message_buffer.add_message(message)



class MessageUpdatesHandler(tornado.web.RequestHandler):
    """Long-polling request for new messages.

    Waits until new messages are available before returning anything.
    """
    async def post(self):            
        cursor = self.get_argument("cursor", None)
        messages = global_message_buffer.get_messages_since(cursor)    
        while not messages and global_message_buffer.should_process_input_from_user:
            # Save the Future returned here so we can cancel it in
            # on_connection_close.
            self.wait_future = global_message_buffer.cond.wait()        
            try:
                await self.wait_future
            except asyncio.CancelledError:
                return        
            messages = global_message_buffer.get_messages_since(cursor)
            global_message_buffer.should_process_input_from_user = False            
        
        if self.request.connection.stream.closed():
            return    
        # let's wait for message from the Java server
        if not messages:
            body = None
            if global_message_buffer.is_new_session and not global_message_buffer.user_id_validation:
                body = "[IPA]: Please, enter your MKT id and press 'Post' button below to start:"    
            else:
                body = global_message_buffer.user_id_validation
        
            message = {
               "id": str(uuid.uuid4()),    
               "body": body,
            }
            message["html"] = tornado.escape.to_unicode(
            self.render_string("message.html", message=message))
            global_message_buffer.add_message(message) 
            cursor = self.get_argument("cursor", None)
            messages = global_message_buffer.get_messages_since(cursor)
            global_message_buffer.should_process_input_from_user = True

        self.write(dict(messages=messages))
        


    def on_connection_close(self):
        self.wait_future.cancel()


def main():
    parse_command_line()

    app = tornado.web.Application(
        [
            (r"/", MainHandler),
            (r"/a/message/new", MessageNewHandler),
            (r"/a/message/updates", MessageUpdatesHandler),
        ],
        cookie_secret="__TODO:_GENERATE_YOUR_OWN_RANDOM_VALUE_HERE__",
        template_path=os.path.join(os.path.dirname(__file__), "templates"),
        static_path=os.path.join(os.path.dirname(__file__), "static"),
        xsrf_cookies=True,
        debug=options.debug,
    )   

    app.listen(options.port)
    tornado.ioloop.IOLoop.current().start()


if __name__ == "__main__":
    main()
