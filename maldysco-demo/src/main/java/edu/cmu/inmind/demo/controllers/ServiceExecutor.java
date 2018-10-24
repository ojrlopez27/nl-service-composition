package edu.cmu.inmind.demo.controllers;

import edu.cmu.inmind.demo.apis.UndefinedService;
import edu.cmu.inmind.demo.common.ServiceMethod;
import edu.cmu.inmind.demo.common.Utils;
import edu.cmu.inmind.demo.model.WorkingMemory;
import edu.cmu.inmind.demo.orchestrator.DemoOrchestrator;
import edu.cmu.inmind.demo.pojos.AbstractServicePOJO;
import edu.cmu.inmind.demo.pojos.NERPojo;
import edu.cmu.inmind.demo.apis.GenericService;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by oscarr on 8/10/18.
 */
public class ServiceExecutor {
    private final String TAG = ServiceExecutor.class.getSimpleName();
    private WorkingMemory wm;
    private boolean userScannner = true;
    private Class candidate;
    private DemoOrchestrator orchestrator;
    private static Scanner scanner = new Scanner(System.in);
    private static final double upperThreshold = Double.parseDouble(CommonUtils.getProperty("service.executor.threshold.upper"));
    private static final double lowerThreshold = Double.parseDouble(CommonUtils.getProperty("service.executor.threshold.lower"));
    private static final double delta = Double.parseDouble(CommonUtils.getProperty("service.executor.delta"));

    public ServiceExecutor() {
        this.wm = WorkingMemory.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void setOrchestrator(DemoOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public void pick(String QoSfeature, Class<? extends GenericService> genericService){
        List<ServiceMethod> implementations = Utils.getImplementationsOf(genericService, wm.getServiceMethod());
        ServiceMethod method = Utils.selectService(QoSfeature, implementations);
        List<NERPojo> entities = NERController.extractEntities(wm.getConcreteAction());
        Object[] args = Utils.matchEntitiesToArgs(entities, method.getServiceMethod(), method.getParams());
        for(int idx = 0; idx < args.length; idx++){
            String[] argDescription = Utils.getArgDescAnnotation(method.getServiceMethod(), idx);
            Type type = method.getParams()[idx];
            Object arg = args[idx];
            if(arg == null){
                // first, let's look for existing objects (parameters) in the working memory
                args[idx] = wm.getResult(type.getTypeName() + "." + argDescription[0]);
                if(args[idx] == null){
                    String question = "Missing criteria (argument). Please indicate: " + argDescription[1];
                    Log4J.error(TAG, question);
                    String answer = scanner.nextLine();
                    args[idx] = Utils.getObjectFromAnswer(answer, type);
                }
            }
            wm.addResult(type.getTypeName() + "." + argDescription[0], args[idx]);
        }
        wm.addResult(genericService.getName(), Utils.executeMethod(method, args));
    }

    public void setCandidate(String QoSfeature, Class<? extends GenericService> genericService){
        candidate = genericService;
    }

    /**
     * this method works similar to a Dialogue State Tracker: it defines two thresholds, so:
     * if similarity >= t1(0.5) => if no other service is high condidente (delta >= 0.01) then 100% confident about picking this service method.
     * if t2(0.2) <= similarity <= t1(0.5) => ask for confirmation
     * if similarity <= t2(0.2) => the service does not exist or it is not available
     * @param abstractServices
     * @param serviceMap
     * @return
     */
    public ServiceMethod getServiceMethod(List<AbstractServicePOJO> abstractServices, Map<String, ServiceMethod> serviceMap,
                                          boolean useScanner) {
        this.userScannner = useScanner;
        //assumption: we are considering (for now) only 2 candidates:
        double first = abstractServices.get(0).getSimilarity();
        double second = abstractServices.get(1).getSimilarity();
        String descFirst = abstractServices.get(0).getServiceDescription();
        String descSecond = abstractServices.get(1).getServiceDescription();
        ServiceMethod smFirst = serviceMap.get(descFirst);
        ServiceMethod smSecond = serviceMap.get(descSecond);

        // if service descriptions do not match any service, that means that sent2vec picked probably a wrong service
        // or at least one that cannot be mapped as an abstract service. We will use this for precision and recall
        if(smFirst == null && smSecond == null){
            return new ServiceMethod(UndefinedService.class, descFirst);
        }else if(smSecond == null){
            smSecond = smFirst;
        }else if(smFirst == null){
            smFirst = smSecond;
        }
        if( !useScanner ) return smFirst;
        if(first >= upperThreshold){
            if( smFirst.getServiceMethod().getName().equals(smSecond.getServiceMethod().getName())
                    && Math.abs(first - second) <= delta && !descFirst.equals(descSecond) ){
                Log4J.error(TAG, String.format("I need to disambiguate which service you need. Type '1' " +
                                "if you need '%s' [similarity = %s] or type '2' if you need '%s' [similarity = %s]: ",
                        first, descFirst, second, descSecond));
                return scanner.nextLine().equals("1") ? smFirst : smSecond;
            }
            return smFirst;
        }else if(second >= upperThreshold){
            return smSecond;
        }else if(first >= lowerThreshold || second >= lowerThreshold){
            Log4J.error(TAG, String.format("[similarity = %s] I am not sure if I understood correctly. You need this " +
                    "service '%s', right? type Y (yes) or simply re-phrase it:", first, first >= lowerThreshold ?
                    descFirst : descSecond));
            //TODO: we need to call again sent2vec for the second desc
            return scanner.nextLine().equalsIgnoreCase("Y") ? smFirst : smFirst;
        }else{
            Log4J.error(TAG, String.format("[similarity = %s] Sorry, this service is not available at this moment!", first));
        }
        return smFirst;
    }
}
