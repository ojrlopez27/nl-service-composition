package edu.cmu.inmind.composition.pojos;

/**
 * Created by oscarr on 8/8/18.
 */
public class LocationPOJO extends GenericPOJO {
    private String place;

    public LocationPOJO(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
