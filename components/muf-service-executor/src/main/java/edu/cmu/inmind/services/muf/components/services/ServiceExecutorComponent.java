package edu.cmu.inmind.services.muf.components.services;

import edu.cmu.inmind.multiuser.controller.blackboard.Blackboard;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardEvent;
import edu.cmu.inmind.multiuser.controller.blackboard.BlackboardSubscription;
import edu.cmu.inmind.multiuser.controller.common.Constants;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import edu.cmu.inmind.multiuser.controller.plugin.PluggableComponent;
import edu.cmu.inmind.multiuser.controller.plugin.StateType;
import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.utils.Pair;
import edu.cmu.inmind.services.commons.GenericPOJO;
import edu.cmu.inmind.services.muf.components.services.executor.ServiceExecutor;
import edu.cmu.inmind.services.muf.components.services.executor.ServiceExecutorController;
import edu.cmu.inmind.services.muf.inputs.ServiceExecutorInput;
import edu.cmu.inmind.services.muf.inputs.ServiceRegistryInput;
import edu.cmu.inmind.services.muf.outputs.ServiceExecutorOutput;
import edu.cmu.inmind.services.muf.outputs.ServiceRegistryOutput;

import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SE_EXECUTE_OSGI_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SE_RESP_EXECUTE_OSGI_SERVICE;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_GET_SERVICE_BY_POJO;
import static edu.cmu.inmind.services.muf.commons.Constants.MSG_SR_RESP_GET_SERVICE_BY_POJO;

@StateType(state = Constants.STATEFULL)
@BlackboardSubscription(messages = {
        MSG_SE_EXECUTE_OSGI_SERVICE,            // SE executes an OSGi service
        MSG_SR_RESP_GET_SERVICE_BY_POJO         // SE asks for a Service Pair, so, it listens to the response from SR
})
public class ServiceExecutorComponent extends PluggableComponent {

    ServiceExecutorController serviceExecutorController;

    @Override
    public void startUp() {
        super.startUp();
        Log4J.info(this, "Inside ServiceExecutorComponent.startUp");
        serviceExecutorController = new ServiceExecutorController();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        Log4J.info(this, "Inside ServiceExecutorComponent.shutDwon");
    }

    @Override
    public void onEvent(Blackboard blackboard, BlackboardEvent blackboardEvent) throws Throwable {
        Log4J.info(this, "Inside ServiceExecutorComponent.onEvent");

        try {

            // read the input command
            String serviceExecutorInputCommand = blackboardEvent.getId();
            Log4J.info(this, "ServiceExecutorInputCommand: " + serviceExecutorInputCommand);

            switch (serviceExecutorInputCommand) {

                case MSG_SE_EXECUTE_OSGI_SERVICE: {

                    // to execute a service, we need the service pair
                    ServiceExecutorInput serviceExecutorInput = (ServiceExecutorInput) blackboardEvent.getElement();
                    GenericPOJO servicePOJO = serviceExecutorInput.getGenericPOJO();

                    // obtain the service pair from the service registry
                    ServiceRegistryInput serviceRegistryInput
                            = new ServiceRegistryInput.GetServiceByPOJOBuilder(MSG_SR_GET_SERVICE_BY_POJO)
                            .setServicePOJO(servicePOJO)
                            .build();

                    // post the message to the blackboard to get the service pair
                    blackboard.post(this, MSG_SR_GET_SERVICE_BY_POJO, serviceRegistryInput);
                    break;
                }

                case MSG_SR_RESP_GET_SERVICE_BY_POJO: {

                    // retrieve the service pair sent by the service registry
                    ServiceRegistryOutput serviceRegistryOutput = (ServiceRegistryOutput) blackboardEvent.getElement();
                    GenericPOJO servicePOJO = serviceRegistryOutput.getServicePOJO();
                    Pair<BundleImplInfo, BundleApiInfo> servicePair = serviceRegistryOutput.getServicePair();

                    // execute the OSGi service
                    GenericPOJO resultPOJO = ServiceExecutor.execute(servicePOJO, servicePair);

                    // update the caller that the service has been executed
                    ServiceExecutorOutput serviceExecutorOutput =
                            new ServiceExecutorOutput.ExecuteOSGIServiceRespBuilder(MSG_SE_RESP_EXECUTE_OSGI_SERVICE)
                            .setResult(resultPOJO)
                            .build();

                    // post the message to the blackboard so that the caller can handle the service execution result
                    blackboard.post(this, MSG_SE_RESP_EXECUTE_OSGI_SERVICE, serviceExecutorOutput);
                    break;
                }
            }

            // output..

        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
