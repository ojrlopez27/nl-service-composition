package edu.cmu.inmind.demo.pojos;

import edu.cmu.inmind.services.commons.GenericPOJO;

import java.util.List;

/**
 * Created by sakoju on 8/8/20.
 */
public class GroundTransportationPOJO extends GenericPOJO {
    protected GroundTransportationPOJO(GTPojoBuilder builder) {
        super(builder);
    }

    @Override
    public List<Object> transform() {
        return super.transform();
    }

    public class GTPojoBuilder extends PojoBuilder
    {

        public GTPojoBuilder(String serviceId, String alias, String methodName) {
            super(serviceId, alias, methodName);
        }

        @Override
        public GroundTransportationPOJO build() {
            return new GroundTransportationPOJO(this);
        }
    }
}
