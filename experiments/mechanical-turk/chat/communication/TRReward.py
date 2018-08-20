class  TRReward(object):
    deal = None
    humanAmount = None
    agentAmount = None
    humanPoint = None
    agentPoint = None
    # add as many attributes as you need
    def __init__(self, deal, humanAmount, agentAmount, humanPoint, agentPoint):
        self.deal = deal
        self.humanAmount = humanAmount
        self.agentAmount = agentAmount
        self.humanPoint = humanPoint
        self.agentPoint = agentPoint
    def toString(self):
        obj = {}
        obj["deal"] = self.deal
        obj["humanAmount"] = self.humanAmount
        obj["agentAmount"] = self.agentAmount
        obj["humanPoint"] = self.humanPoint
        obj["agentPoint"] = self.agentPoint
        return str(obj)
