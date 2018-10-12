package edu.cmu.inmind.services.muf.inputs;

public abstract class CommandInput {

    protected String command;

    protected CommandInput(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    protected void setCommand(String command) {
        this.command = command;
    }

    protected void validateCommand(String command) {
        if (!isCommand(command)) {
            throw new UnsupportedOperationException("Command not " + command);
        }
    }

    private boolean isCommand(String command) {
        return this.command.equals(command);
    }

}
