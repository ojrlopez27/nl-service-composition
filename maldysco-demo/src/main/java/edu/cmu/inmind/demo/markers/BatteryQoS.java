package edu.cmu.inmind.demo.markers;

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
public @interface BatteryQoS {
    String minBatteryLevel() default Constants.WORKS_WITH_LOW_CHARGE;
}
