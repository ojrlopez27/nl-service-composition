package edu.cmu.inmind.demo.pojos;

import edu.cmu.inmind.services.commons.GenericPOJO;

import java.util.List;

/**
 * Created by oscarr on 8/8/18.
 */
public class FlightPOJO extends GenericPOJO {
    String flight="";
    protected FlightPOJO(FlightPojoBuilder builder) {
        super(builder);
        this.flight =builder.flight;
    }

    @Override
    public List<Object> transform() {
        return super.transform();
    }
}
