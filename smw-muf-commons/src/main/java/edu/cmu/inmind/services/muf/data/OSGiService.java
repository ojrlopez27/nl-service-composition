package edu.cmu.inmind.services.muf.data;

import java.util.Date;

public class OSGiService {

    private String name;
    private String level;
    private Date deploymentDate;
    private Boolean autoUpdate;

    public OSGiService(String serviceName, String serviceLevel) {
        this.name = serviceName;
        this.level = serviceLevel;
    }

    public OSGiService(String serviceName, String serviceLevel, Date deploymentDate, Boolean autoUpdate) {
        this.name = serviceName;
        this.level = serviceLevel;
        this.deploymentDate = deploymentDate;
        this.autoUpdate = autoUpdate;
    }

    public String getServiceName() {
        return name;
    }

    public void setServiceName(String serviceName) {
        this.name = serviceName;
    }

    public String getServiceLevel() {
        return level;
    }

    public void setServiceLevel(String serviceLevel) {
        this.level = serviceLevel;
    }

    public Date getDeploymentDate() {
        return deploymentDate;
    }

    public void setDeploymentDate(Date deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public Boolean getAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(Boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    @Override
    public String toString() {
        return "OSGiService{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", deploymentDate=" + deploymentDate +
                ", autoUpdate=" + autoUpdate +
                '}';
    }
}
