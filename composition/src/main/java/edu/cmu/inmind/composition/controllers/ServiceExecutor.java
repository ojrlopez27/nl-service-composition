package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.model.WorkingMemory;

import java.util.List;

/**
 * Created by oscarr on 8/10/18.
 */
public class ServiceExecutor {
    private WorkingMemory wm;
    private Class candidate;

    public ServiceExecutor(WorkingMemory wm) {
        this.wm = wm;
    }

    public void pick(String QoSfeature, Class<? extends GenericService> genericService){
        List<ServiceMethod> implementations = Utils.getImplementationsOf(genericService, wm.getServiceMethod());
        ServiceMethod method = Utils.selectService(QoSfeature, implementations);
        //TODO: extract method parameters....
        Utils.executeMethod(method);
    }

    public void setCandidate(String QoSfeature, Class<? extends GenericService> genericService){
        candidate = genericService;
    }
}
