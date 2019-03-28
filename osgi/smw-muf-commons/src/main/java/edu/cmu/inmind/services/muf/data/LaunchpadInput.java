package edu.cmu.inmind.services.muf.data;

public class LaunchpadInput {

    private String command;
    private OSGiService osGiService;

    public LaunchpadInput(String command, OSGiService osGiService) {
        this.command = command;
        this.osGiService = osGiService;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public OSGiService getOsGiService() {
        return osGiService;
    }

    public void setOsGiService(OSGiService osGiService) {
        this.osGiService = osGiService;
    }

    @Override
    public String toString() {
        return "LaunchpadInput{" +
                "command='" + command + '\'' +
                ", osGiService=" + osGiService +
                '}';
    }
}
