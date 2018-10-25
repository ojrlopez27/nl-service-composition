package edu.cmu.inmind.demo.pojos;

import edu.cmu.inmind.services.commons.GenericPOJO;

public class FlightPojoBuilder extends GenericPOJO.PojoBuilder{

    public String getFlight() {
        return flight;
    }

    public FlightPojoBuilder setFlight(String flight) {
        this.flight = flight;
        return this;
    }

    public String flight;
    public FlightPojoBuilder(String serviceId, String alias, String methodName) {
        super(serviceId, alias, methodName);
    }

    @Override
    public FlightPOJO build() {
        return new FlightPOJO(this);
    }

}
