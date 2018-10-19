package edu.cmu.inmind.demo.model;

import edu.cmu.inmind.demo.controllers.SemanticController;
import edu.cmu.inmind.demo.controllers.ServiceExecutor;
import edu.cmu.inmind.demo.pojos.AbstractServicePOJO;
import edu.cmu.inmind.demo.apis.GenericService;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String abstractService;
    private String concreteAction;
    private Map<String, Object> results = new HashMap<>();
    private List<List<AbstractServicePOJO>> candidates = new ArrayList<>();
    private int idxCandidates;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getAbstractService() {
        return abstractService;
    }

    public void setAbstractService(String abstractService) {
        this.abstractService = abstractService;
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

    public void addResult(String key, Object result) {
        results.put(key, result);
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public void setResults(Map<String, Object> results) {
        this.results = results;
    }

    public List<List<AbstractServicePOJO>> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<List<AbstractServicePOJO>> candidates) {
        this.candidates = candidates;
    }

    public String getConcreteAction() {
        return concreteAction;
    }

    public void setConcreteAction(String concreteAction) {
        this.concreteAction = concreteAction;
    }

    public List<AbstractServicePOJO> getAbstractServices() {
        return candidates.get(idxCandidates++);
    }

    public void print(String message){
        Log4J.trace(this, message);
    }

    public Object getResult(String key) {
        Object obj = results.get(key);
        if(obj != null) return obj;
        int pos = key.lastIndexOf(".");
        String pre = key.substring(0, pos);
        String post = key.substring(pos + 1);
        for(String synonym : SemanticController.getSynonyms(post)){
            obj = results.get(pre + "." + synonym);
            if(obj != null) return obj;
        }
        return null;
    }
}
