package edu.cmu.inmind.services.muf.components.middleware.commons;

import java.util.ArrayList;
import java.util.List;

public class ClassExtractor {

    public static List<Class<?>> getAllSuperClasses(Class<?> clazz) {
        Class<?> klass = clazz;
        List<Class<?>> superClasses = new ArrayList<>();

        // if it a valid class
        //while (validClass(klass)) {
        while (klass != null
                && klass != Object.class
            //    && (!klass.getName().startsWith("java.")
            ) {

            // add it to the list of the classes
            superClasses.add(klass);

            // iterate over all interfaces of the class
            // add those that are not a Core Java Api
            superClasses.addAll(getValidInterfaces(klass));

            // move to the super class
            klass = klass.getSuperclass();
        }
        return superClasses;
    }

    public static List<Class<?>> getValidInterfaces(Class<?> clazz) {
        List<Class<?>> interfaces = new ArrayList<>();
        for (Class<?> intrface : clazz.getInterfaces()) {
            if(intrface.getName().startsWith("java.")) continue;
            interfaces.add(intrface);
        }
        return interfaces;
    }

    /*
    private static boolean validClass(Class<?> clazz) {
        return (clazz != null && clazz != Object.class && !isCoreJavaAPIs(clazz));
    }

    private static boolean isCoreJavaAPIs(Class clazz) {
        System.out.println("Class: " + clazz);
        //return (clazz != null && clazz.getName() != null && clazz.getName().startsWith("java."));
        return Boolean.FALSE;
    }
*/
}
