package edu.cmu.inmind.services.pojos;

import edu.cmu.inmind.services.commons.GenericPOJO;

/**
 * Created by oscarr on 8/8/18.
 */
public class LocationPOJO extends GenericPOJO {
    private String place;

    protected LocationPOJO(LocationPOJOBuilder locationPOJOBuilder) {
        super(locationPOJOBuilder);
        this.place = locationPOJOBuilder.place;
    }

    public String getPlace() {
        return place;
    }

    public class LocationPOJOBuilder extends PojoBuilder {
        private String place;

        public LocationPOJOBuilder(String serviceId, String alias, String methodName) {
            super(serviceId, alias, methodName);
        }

        public LocationPOJOBuilder setPlace(String place) {
            this.place = place;
            return this;
        }

        public LocationPOJO build() {
            return new LocationPOJO(this);
        }
    }
}
