package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.composition.common.Constants;
import edu.cmu.inmind.composition.common.Node;
import edu.cmu.inmind.composition.common.ServiceMethod;
import edu.cmu.inmind.composition.common.Utils;
import edu.cmu.inmind.composition.model.WorkingMemory;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oscarr on 8/8/18.
 */
public class CompositionController {

    // AS: Abstract Services, GS: Grounded Services
    private static WorkingMemory wm;
    private static Rules rulesAS, rulesGS;
    private static RulesEngine rulesEngineAS, rulesEngineGS;
    private static Facts factsAS;
    private static List<Rule> ruleListAS, ruleListGS;

    public static void init(){
        wm = new WorkingMemory();
        rulesAS = new Rules();
        rulesGS = new Rules();
        factsAS = new Facts();
        rulesEngineAS = new DefaultRulesEngine();
        rulesEngineGS = new DefaultRulesEngine();
        ruleListAS = new ArrayList<>();
        ruleListGS = new ArrayList<>();
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
    public static void addStep(String step) {
        String ruleName = Utils.getRuleName(step);
        ruleListAS.add(
            new MVELRule()
                .name(ruleName)
                .description(step)
                .priority(1)
                .when( "wm.command == wm.lastRuleName")
                .then("System.out.println(\"Triggering '" + ruleName + "'...\"); ")
                .then(String.format("wm.command = \"%s\"; ", ruleName))
                .then(String.format("wm.abstractServices.add(\"%s\");", step))
                .then(String.format("wm.lastRuleName = \"%s\"; ", ruleName))
        );
    }

    public static CompositeService generateCompositeServiceRequest() {
        System.out.println("\n\n======== ABSTRACT SERVICES ===========");
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




    public static void execute(CompositeService compositeService, Map<String, ServiceMethod> serviceMap,
                               List<String> abstractServices) {

        System.out.println("\n\n======== GROUNDED SERVICES ===========");
        for (String abstractService : abstractServices) {
            ServiceMethod serviceMethod = serviceMap.get(abstractService);
            wm.setService(serviceMethod.getServiceClass());
            wm.setServiceMethod(serviceMethod.getServiceMethod());
            fireRulesGS();
        }
    }


    public static class CompositeService {
        private List<Node> nodes = new ArrayList();

        private CompositeService(CompositeServiceBuilder builder){
            this.nodes = builder.nodes;
        }

        public List<Node> getNodes() {
            return nodes;
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
