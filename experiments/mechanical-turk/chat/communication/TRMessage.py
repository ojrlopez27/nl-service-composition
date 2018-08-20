class TRMessage(object):
    userOutputId = None
    agentOutput = None
    speechAct = None
    entities = None
    clauses =None

    # add as many attributes as you need

    def __init__(self, userOutputId, agentOutput,clauses, speechAct, entities):
        self.userOutputId = userOutputId
        self.agentOutput = agentOutput
        self.speechAct = speechAct
        self.entities = entities
        self.clauses=clauses


    def toString(self):
        obj = {}
        obj["userOutputId"] = self.userOutputId
        obj["agentOutput"] = self.agentOutput
        obj["speechAct"] = self.speechAct
        obj["entities"] = self.entities
        obj["clauses"]=self.clauses
        return str(obj)
