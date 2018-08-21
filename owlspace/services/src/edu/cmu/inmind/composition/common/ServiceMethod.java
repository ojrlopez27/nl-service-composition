package edu.cmu.inmind.composition.common;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by oscarr on 8/9/18.
 */
public class ServiceMethod {
    private Class serviceClass;
    private Method serviceMethod;
    private Type[] params;

    public ServiceMethod(Class serviceClass, Method serviceMethod, Type[] params) {
        this.serviceClass = serviceClass;
        this.serviceMethod = serviceMethod;
        this.params = params;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public Type[] getParams() {
        return params;
    }

    public void setParams(Type[] params) {
        this.params = params;
    }
}
