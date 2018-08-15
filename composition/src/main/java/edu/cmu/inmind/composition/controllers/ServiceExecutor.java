package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.model.WorkingMemory;
import edu.cmu.inmind.composition.pojos.NERPojo;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

/**
 * Created by oscarr on 8/10/18.
 */
public class ServiceExecutor {
    private WorkingMemory wm;
    private Class candidate;
    private Scanner scanner;

    public ServiceExecutor(WorkingMemory wm) {
        this.wm = wm;
        this.scanner = new Scanner(System.in);
    }

    public void pick(String QoSfeature, Class<? extends GenericService> genericService){
        List<ServiceMethod> implementations = Utils.getImplementationsOf(genericService, wm.getServiceMethod());
        ServiceMethod method = Utils.selectService(QoSfeature, implementations);
        List<NERPojo> entities = NERController.extractEntities(wm.getAbstractService());
        Object[] args = Utils.matchEntitiesToArgs(entities, method.getParams());
        for(int idx = 0; idx < args.length; idx++){
            String[] argDescription = Utils.getArgDescAnnotation(method.getServiceMethod(), idx);
            Type type = method.getParams()[idx];
            Object arg = args[idx];
            if(arg == null){
                String question = argDescription[1];
                System.out.println(question);
                String answer = scanner.nextLine();
                args[idx] = Utils.getObjectFromAnswer(answer, type);
            }
            wm.addResult(type.getTypeName() + "." + argDescription[0], args[idx]);
        }
        wm.addResult(genericService.getName(), Utils.executeMethod(method, args));
    }

    public void setCandidate(String QoSfeature, Class<? extends GenericService> genericService){
        candidate = genericService;
    }
}
