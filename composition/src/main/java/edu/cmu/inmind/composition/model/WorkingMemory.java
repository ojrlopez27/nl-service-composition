package edu.cmu.inmind.composition.model;

import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.controllers.ServiceExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 8/10/18.
 */
public class WorkingMemory {
    private String goal;
    private String command;
    private String lastRuleName;
    private String battery;
    private String wifi;
    private ServiceExecutor executor;
    private Class<? extends GenericService> service;
    private Method serviceMethod;
    private List<String> abstractServices = new ArrayList();

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getAbstractServices() {
        return abstractServices;
    }

    public void setAbstractServices(List<String> abstractServices) {
        this.abstractServices = abstractServices;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getLastRuleName() {
        return lastRuleName;
    }

    public void setLastRuleName(String lastRuleName) {
        this.lastRuleName = lastRuleName;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public ServiceExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ServiceExecutor executor) {
        this.executor = executor;
    }

    public Class<? extends GenericService> getService() {
        return service;
    }

    public void setService(Class<? extends GenericService> service) {
        this.service = service;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}
