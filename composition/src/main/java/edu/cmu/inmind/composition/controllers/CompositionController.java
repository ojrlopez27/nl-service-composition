package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.common.Node;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.model.WorkingMemory;
import edu.cmu.inmind.composition.pojos.AbstractServicePOJO;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oscarr on 8/8/18.
 */
public class CompositionController {
    private static final String TAG = CompositionController.class.getSimpleName();

    // AS: Abstract Services, GS: Grounded Services
    private static WorkingMemory wm;
    private static Rules rulesAS, rulesGS;
    private static RulesEngine rulesEngineAS, rulesEngineGS;
    private static Facts factsAS;
    private static List<Rule> ruleListAS, ruleListGS;

    public static void init(){
        Log4J.debug(TAG, "Initializing CompositionController...");
        wm = new WorkingMemory();
        rulesAS = new Rules();
        rulesGS = new Rules();
        factsAS = new Facts();
        rulesEngineAS = new DefaultRulesEngine();
        rulesEngineGS = new DefaultRulesEngine();
        ruleListAS = new ArrayList<>();
        ruleListGS = new ArrayList<>();
        NERController.init();
    }

    public static void addGoal(String goal) {
        // create some facts
        wm.setGoal(goal);
        factsAS.put("wm", wm);
        // we do this only the first time
        wm.setLastRuleName(goal);
        wm.setCommand(goal);
    }

    /**
     * This is just a simple chaining of rules.
     * @param step
     */
    public static void addStep(String step, String abstracServiceCandidates) {
        String ruleName = Utils.getRuleName(step);
        List<AbstractServicePOJO> candidates = Utils.extractAbstractServices(abstracServiceCandidates);
        wm.getCandidates().add(candidates);
        ruleListAS.add(
            new MVELRule()
                .name(ruleName)
                .description(step)
                .priority(1)
                .when( "wm.command == wm.lastRuleName")
                .then("wm.print(\"Triggering '" + ruleName + "'...\"); ")
                .then(String.format("wm.command = \"%s\"; ", ruleName))
                .then(String.format("wm.abstractService = \"%s\";", step))
                .then(String.format("wm.lastRuleName = \"%s\"; ", ruleName))
        );
    }

    public static CompositeService generateCompositeServiceRequest() {
        CompositeServiceBuilder builder = null;
        // create a rule set
        for(Rule rule : ruleListAS){
            rulesAS.register(rule);
            if(builder == null) builder = new CompositeServiceBuilder(rule.getDescription());
            else builder.and(rule.getDescription());
        }
        return builder.build();
    }

    /**
     * This method will put in WM the list of abastract services
     */
    public static void fireRulesAS(){
        //create a default rules engine and fire rules on known facts
        rulesEngineAS.fire(rulesAS, factsAS);
    }


    /**
     * We can also simulate here some changing conditions like gradual battery draining, or
     * weak/strong wifi access changes.
     */
    public static void addPhoneStatusToWM(){
        wm.setBattery(Constants.PHONE_HIGH_BATTERY);
        wm.setWifi(Constants.WIFI_ON);
        wm.setExecutor(new ServiceExecutor(wm));
        wm.setCommand(Constants.CHECK_BATTERY);
    }

    /**
     * Heuristic rules for grounding services (given a set of abstract services) based on
     * non-functional QoS features (i.e., battery consumption, connectivity, availability, etc.)
     */
    public static void createRulesForGroundingServices() {
        ruleListGS.add(
                new MVELRule()
                        .name("if-battery-low-then-low-consumption-rule")
                        .description("if battery is low, then look for a service method with low battery consumption")
                        .priority(1)
                        .when( String.format("wm.battery == \"%s\" && wm.command == \"%s\"",
                                Constants.PHONE_LOW_BATTERY, Constants.CHECK_BATTERY) )
                        .then( String.format("wm.executor.pick(\"%s\", wm.service); ", Constants.LOW_BATTERY_SERVICE) )
        );

        ruleListGS.add(
                new MVELRule()
                        .name("if-battery-high-then-check-wifi-rule")
                        .description("if battery is high, then check connectivity")
                        .priority(1)
                        .when( String.format("wm.battery == \"%s\" && wm.command == \"%s\"",
                                Constants.PHONE_HIGH_BATTERY, Constants.CHECK_BATTERY) )
                        .then( String.format("wm.command = \"%s\"", Constants.CHECK_WIFI) )
                        .then( String.format("wm.executor.setCandidate(\"%s\", wm.service); ", Constants.PHONE_HIGH_BATTERY) )
        );

        ruleListGS.add(
                new MVELRule()
                        .name("if-wifi-on-then-pick-remote-service-rule")
                        .description("if wifi is ON then pick a remote service")
                        .priority(1)
                        .when( String.format("wm.wifi == \"%s\" && wm.command == \"%s\"",
                                Constants.WIFI_ON, Constants.CHECK_WIFI) )
                        .then( String.format("wm.executor.pick(\"%s\", wm.service); ", Constants.PICK_REMOTE_SERVICE) )
        );

        ruleListGS.add(
                new MVELRule()
                        .name("if-wifi-off-then-pick-local-service-rule")
                        .description("if wifi is OF then pick a local service")
                        .priority(1)
                        .when( String.format("wm.wifi == \"%s\" && wm.command == \"%s\"",
                                Constants.WIFI_OFF, Constants.CHECK_WIFI) )
                        .then( String.format("wm.executor.pick(\"%s\", wm.service); ", Constants.PICK_LOCAL_SERVICE) )
        );

        for(Rule rule : ruleListGS){
            rulesGS.register(rule);
        }
    }

    public static void fireRulesGS(){
        //create a default rules engine and fire rules on known facts
        rulesEngineGS.fire(rulesGS, factsAS);
    }




    public static void execute(String concreteAction, Map<String, ServiceMethod> serviceMap) {
        ServiceMethod serviceMethod = ServiceExecutor.getServiceMethod(wm.getAbstractServices(), serviceMap);
        wm.setService(serviceMethod.getServiceClass());
        wm.setServiceMethod(serviceMethod.getServiceMethod());
        wm.setConcreteAction(concreteAction);
        // these rules call the ServiceExecutor.pick() method
        fireRulesGS();
    }


    public static class CompositeService {
        private List<Node> nodes = new ArrayList();
        private int idx = 0;

        private CompositeService(CompositeServiceBuilder builder){
            this.nodes = builder.nodes;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public String getNext() {
            if (idx < nodes.size()) {
                return nodes.get(idx++).getName();
            }
            return "No more services to process!";
        }
    }

    public static class CompositeServiceBuilder{
        private List<Node> nodes = new ArrayList();

        private CompositeServiceBuilder(String request){
            and( request );
        }

        public CompositeServiceBuilder and(String request) {
            nodes.add(new Node(request, Node.NodeType.PLAIN));
            return this;
        }

        public CompositeService build(){
            return new CompositeService(this);
        }
    }


}
