package edu.cmu.inmind.composition.apis;

/**
 * Created by oscarr on 8/17/18.
 */
public interface TrafficService extends GenericService{

    void checkTracffic();

    void calculateShortestRoute();
}
