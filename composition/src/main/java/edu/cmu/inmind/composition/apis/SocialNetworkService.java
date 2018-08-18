package edu.cmu.inmind.composition.apis;

/**
 * Created by oscarr on 8/17/18.
 */
public interface SocialNetworkService extends GenericService {

    void changeNewStatus();

    void messageSomebody();

    void uploadPhoto();
}
