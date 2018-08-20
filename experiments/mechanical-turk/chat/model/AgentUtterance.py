class AgentUtterance(object):

    utterance = None

    def __init__(self, utterance):
        self.utterance = utterance.decode("utf-8")


    def toString(self):
        obj = {}
        obj["utterance"] = self.utterance
        return str(obj)
