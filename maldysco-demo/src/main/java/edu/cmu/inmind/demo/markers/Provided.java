package edu.cmu.inmind.demo.markers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by oscarr on 8/16/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,  ElementType.FIELD})
//TODO: this is a little hack, we need to replace it by a proper retrieval from working memory
public @interface Provided {
    String value();
}
