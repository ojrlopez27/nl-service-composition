class ContextMessage(object):
    initiator = None
    human_context = None
    agent_context = None

    # add as many attributes as you need

    def __init__(self, Initiator, Human_context,Agent_context):
        self.initiator= Initiator
        self.human_context = Human_context
        self.agent_context = Agent_context


    def toString(self):
        obj = {}
        obj["initiator"] = self.initiator
        obj["human_context"] = self.human_context
        obj["agent_context"] = self.agent_context
        return str(obj)
