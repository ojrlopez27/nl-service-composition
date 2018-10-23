package edu.cmu.inmind.services.muf.components.services.executor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SEUtils {

    private static Cache<String, Method> methodsHash =
            CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .recordStats()
                    .build();

    public static <T> T executeMethod(Object receiverObj, Method method, boolean isStatic,
                                      Object... parameters) {
        try {
            System.out.println("Method.obj: " + method);
            System.out.println("Method.clr: " + method.getClass().getClassLoader());
            System.out.println("Method.dec: " + method.getDeclaringClass());
            System.out.println("Method.dec.clr: " + method.getDeclaringClass().getClassLoader());

            System.out.println("by methd: " + method.getParameterTypes()[0].getClass().getClassLoader());
            System.out.println("by param: " + parameters[0].getClass().getClassLoader());
            if (isStatic) {
                return (T) method.invoke(null, parameters);
            } else {
                return (T) method.invoke(receiverObj, parameters);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T executeMethod(Object receiverObj, String stringMethod, boolean isStatic,
                                      Object... parameters) {

        Method method = getMethod(receiverObj.getClass(), stringMethod);
        try {
            if (method == null) {
                if (parameters != null && parameters.length > 0) {
                    Class[] parameterTypes = new Class[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        parameterTypes[i] = parameters[i].getClass();
                    }

                    boolean methodFound = Boolean.FALSE;
                    for (int i=0; i<parameters.length; i++) {
                        Class<?> callbackClass = parameters[i].getClass();
                        List<Class<?>> callbackSuperClasses = ClassExtractor.getAllSuperClasses(callbackClass);

                        for (Class<?> callbackSuperClass : callbackSuperClasses) {
                            if (methodFound) break;
                            if (callbackSuperClass.equals(callbackClass)) continue;

                            // if BundleCallback interface is found for the last parameter
                            // then, we know that we've found our desired method,
                            // however, getMethod() wouldn't find it,
                            // so, replacing it with Object class such that getMethod() finds it
                            if ((i == (parameters.length-1))
                                    && callbackSuperClass.getCanonicalName()
                                    .equals(ServiceExecutor.class.getCanonicalName())) {

                                parameterTypes[i] = Object.class;
                            }
                            else {
                                parameterTypes[i] = callbackSuperClass;
                            }

                            if (!methodFound) {
                                try {
                                    Method[] classMethods;
                                    if (receiverObj instanceof Class<?>) {
                                        classMethods = ((Class<?>) receiverObj).getMethods();
                                    } else {
                                        classMethods = receiverObj.getClass().getMethods();
                                    }

                                    method = getMethod(classMethods, stringMethod, parameterTypes);
                                } catch (NoSuchMethodException ignored) {
                                }
                                methodFound = (method != null);
                            }
                        }
                    }
                }

                putMethod(receiverObj.getClass(), stringMethod, method);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return executeMethod(receiverObj, method, isStatic, parameters);
    }

    private static void putMethod(Class clazz, String smethod, Method method ){
        methodsHash.put( clazz.getCanonicalName() + "." + smethod, method);
    }

    private static Method getMethod(Class clazz, String method){
        return methodsHash.getIfPresent( clazz.getCanonicalName() + "." + method );
    }

    private static Method getMethod(Object[] objects, String methodName, Class<?>[] parameterTypes)
            throws NoSuchMethodException {

        if (objects == null) {
            throw new NoSuchMethodException();
        }
        String parameterTypesString = Arrays.toString(parameterTypes);
        for (Object object : objects) {
            Method method = (Method) object;
            if (method.getName().equals(methodName)
                    && Arrays.toString(method.getParameterTypes()).equals(parameterTypesString)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    static class ClassExtractor {

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
}
