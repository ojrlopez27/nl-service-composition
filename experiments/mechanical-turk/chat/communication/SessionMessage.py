class SessionMessage(object):

    requestType = None
    sessionId = None
    url = None
    payload = None
    messageId = None

    def __init__(self, requestType, sessionId, url, payload, messageId):
        self.requestType = requestType
        self.sessionId = sessionId
        self.url = url
        self.payload = payload
        self.messageId = messageId

    def toString(self):
        obj = {}
        obj["requestType"] = self.requestType
        obj["sessionId"] = self.sessionId
        obj["url"] = self.url
        obj["payload"] = self.payload
        obj["messageId"] = self.messageId
        return str(obj)
