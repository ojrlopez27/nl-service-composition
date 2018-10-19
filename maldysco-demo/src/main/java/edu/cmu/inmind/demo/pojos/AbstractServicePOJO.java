package edu.cmu.inmind.demo.pojos;

/**
 * Created by oscarr on 8/15/18.
 */
public class AbstractServicePOJO {
    private double similarity;
    private String serviceDescription;

    public AbstractServicePOJO(double similarity, String serviceDescription) {
        this.similarity = similarity;
        this.serviceDescription = serviceDescription.replace(" , ", ", ");
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
