package edu.cmu.inmind.composition.components.launchpad.muf.client;

public class MUFLaunchpadClientMain {

    public static void main(String args[]){
        MUFCommunicationController communicationController = new MUFCommunicationController();
        communicationController.sendLaunchpadStarter("START");
        String receipt = communicationController.receiveLaunchpadStarter();
        System.out.println(receipt.equals("") ? "NO_RECEIPT" : receipt);
    }




}
