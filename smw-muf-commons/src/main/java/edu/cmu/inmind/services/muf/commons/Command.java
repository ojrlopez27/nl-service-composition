package edu.cmu.inmind.services.muf.commons;

import java.util.List;

public abstract class Command {

    protected String command;

    protected Command(String command) {
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

    protected void validateIfAnyCommand(String[] commands) {
        boolean anyValid = Boolean.FALSE;
        for (String command : commands) {
            anyValid = anyValid || isCommand(command);
        }
        if (!anyValid) {
            throw new UnsupportedOperationException("Command neither of " + commands);
        }
    }

    private boolean isCommand(String command) {
        return this.command.equals(command);
    }

}
