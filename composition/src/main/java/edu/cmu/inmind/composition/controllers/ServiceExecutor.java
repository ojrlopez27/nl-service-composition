package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.model.WorkingMemory;
import edu.cmu.inmind.composition.pojos.AbstractServicePOJO;
import edu.cmu.inmind.composition.pojos.NERPojo;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by oscarr on 8/10/18.
 */
public class ServiceExecutor {
    private WorkingMemory wm;
    private Class candidate;
    private Scanner scanner;
    private static double highThreshold = 0.5;
    private static double lowThreshold = 0.2;
    private static double delta = 0.01;

    public ServiceExecutor(WorkingMemory wm) {
        this.wm = wm;
        this.scanner = new Scanner(System.in);
    }

    public void pick(String QoSfeature, Class<? extends GenericService> genericService){
        List<ServiceMethod> implementations = Utils.getImplementationsOf(genericService, wm.getServiceMethod());
        ServiceMethod method = Utils.selectService(QoSfeature, implementations);
        List<NERPojo> entities = NERController.extractEntities(wm.getConcreteAction());
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

    public static ServiceMethod getServiceMethod(List<AbstractServicePOJO> abstractServices, Map<String, ServiceMethod> serviceMap) {
        //assumption: we are considering (for now) only 2 candidates:
        double first = abstractServices.get(0).getSimilarity();
        double second = abstractServices.get(1).getSimilarity();
        String descFirst = abstractServices.get(0).getServiceDescription();
        String descSecond = abstractServices.get(1).getServiceDescription();
        ServiceMethod smFirst = serviceMap.get(descFirst);
        ServiceMethod smSecond = serviceMap.get(descSecond);
        Scanner scanner = new Scanner(System.in);
        if(first >= highThreshold){
            if( smFirst.getServiceMethod().getName().equals(smSecond.getServiceMethod().getName())
                    && Math.abs(first - second) <= delta ){
                System.out.println(String.format("*** I need to disambiguate which service you need. Type '1' " +
                        "if you need '%s' or type '2' if you need '%s':", descFirst, descSecond));
                return scanner.nextLine().equals("1")? smFirst : smSecond;
            }
            return smFirst;
        }else if(second >= highThreshold ){
            return smSecond;
        }else if(first >= lowThreshold || second >= lowThreshold){
            System.out.println(String.format("*** I am not sure if I understood correctly. You need this service '%s', " +
                    "right? type Y (yes) or simply re-phrase it:", first >= lowThreshold? descFirst : descSecond));
            //TODO: we need to call again sent2vec for the second desc
            return scanner.nextLine().equalsIgnoreCase("Y")? smFirst : smFirst;
        }else{
            System.out.println("*** Sorry, this service is not available at this moment!");
        }
        return smFirst;
    }
}
