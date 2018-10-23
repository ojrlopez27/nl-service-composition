package edu.cmu.inmind.services.commons;

import java.util.List;

/**
 * GenericPOJO is useful to define generic types for parameters and return types in bundles
 *
 * Created by oscarr on 2/12/18.
 */
public class GenericPOJO {

    private static final String ERR_TRANSFORM_UNSUPPORTED = "GenericPOJO cannot be transformed: no valid operation.";

    /**
     * Unique identifier for the bundle/service. This must correspond to the id defined in the BundleAPI
     * annotation at the bundle api level.
     */
    protected String serviceId;

    /**
     * This is kind of alias for the bundle. This is an arbitrary value assigned by the developer
     * when implementing the bundle. For instance, for calculator bundle API, there could be multiple
     * bundle implementations, each one with a different alias, e.g., calculator-integer, calculator-long, etc.
     * This must correspond to the alias in the BundleImpl annotation
     * at the bundle implementation level.
     */
    protected String alias;

    /**
     * Unique identifier for the requested method. If the POJO is created from top level
     * layers (e.g., UI, Android Activity, etc.) then a method id has to be provided.
     */
    protected String methodName;

    protected GenericPOJO(PojoBuilder builder) {
        this.serviceId = builder.serviceId;
        this.methodName = builder.methodName;
        this.alias = builder.alias;
    }

    /**
     * Returns the unique identifier for the bundle/service.
     * It must correspond to the id defined in the BundleAPI.
     * annotation at the bundle api level.
     *
     * @return the bundle/service's unique identifier.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Returns the unique identifier for the requested method. If the POJO is created from top level
     * layers (e.g., UI, Android Activity, etc.) then a method id has to be provided.
     *
     * @return the method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Returns the arbitrary value assigned by the developer as the bundle alias when implementing the bundle.
     * For instance, for calculator bundle API, there could be multiple bundle implementations,
     * each one with a different alias, e.g., calculator-integer, calculator-long, etc.
     * It must correspond to the alias in the BundleImpl annotation
     * at the bundle implementation level.
     *
     * @return the bundle's alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns a <code>List</code> of <code>Object</code> for the parameters
     * for the method as specified by the method name that describes this POJO.
     * The order of the parameters needs to be ensured.
     * @return
     */
    public List<Object> transform() {
        throw new UnsupportedOperationException(ERR_TRANSFORM_UNSUPPORTED);
    }

    /**
     * Created by oscarr on 4/25/18.
     */
    public static class PojoBuilder {
        protected String serviceId;
        protected String alias;
        protected String methodName;

        public PojoBuilder(String serviceId, String alias, String methodName) {
            this.serviceId = serviceId;
            this.alias = alias;
            this.methodName = methodName;
        }

        public GenericPOJO build(){
            return new GenericPOJO(this);
        }
    }
}

