package edu.cmu.inmind.services.muf.components.middleware.commons;

/**
 *
 * The service locator pattern is a design pattern used in software development to encapsulate the
 * processes involved in obtaining a service with a strong abstraction layer. This pattern uses a
 * central registry known as the "service locator", which on request returns the information
 * necessary to perform a certain task.
 * @see @link https://msdn.microsoft.com/en-us/library/ff648968.aspx
 *
 * Created by oscarr on 8/10/15.
 */
public final class ResourceLocator {

    // TODO: add more attributes
    private static ResourceLocator instance;
    private static String TAG ="ResourceLocator";

    // TODO: initialize more attributes
    private ResourceLocator() {
    }

    public static ResourceLocator getInstance() {
        if (instance == null) {
            instance = new ResourceLocator();
        }
        return instance;
    }

    // TODO: add implementation
    public void addActivity(Object subscriber) { }

    // TODO: release more resources
    public void destroy() {
        System.gc();
    }
}