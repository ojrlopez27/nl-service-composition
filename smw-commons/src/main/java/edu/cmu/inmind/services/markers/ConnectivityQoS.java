package edu.cmu.inmind.services.markers;

import edu.cmu.inmind.services.commons.Constants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by oscarr on 8/10/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConnectivityQoS {
    String wifiStatus() default Constants.REQUIRES_WIFI_CONNECTIVITY;
}
