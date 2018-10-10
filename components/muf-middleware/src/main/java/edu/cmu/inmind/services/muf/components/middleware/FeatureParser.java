package edu.cmu.inmind.services.muf.components.middleware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import edu.cmu.inmind.osgi.commons.utils.CommonUtils;
import edu.cmu.inmind.osgi.commons.utils.ErrorMessages;
import edu.cmu.inmind.osgi.commons.utils.OSGiCoreException;

import static edu.cmu.inmind.osgi.apis.Constants.FEATURE_ID;
import static edu.cmu.inmind.osgi.apis.Constants.FEATURE_DESCRIPTION;
import static edu.cmu.inmind.osgi.apis.Constants.FEATURE_KEYWORDS;
import static edu.cmu.inmind.osgi.apis.Constants.FEATURE_KEYWORDS_DELIM;

/**
 * Created by adangi.
 */
public class FeatureParser {

    private Method method;
    private Class declaringClass;
    private Map<String, Map<String, String>> featureMap;

    public FeatureParser(Method method, Class declaringClass)
            throws InvocationTargetException, IllegalAccessException {

        this.method = method;
        this.declaringClass = declaringClass;
        this.featureMap = (Map<String, Map<String, String>>) method.invoke(CommonUtils.createInstance(declaringClass), (Object[]) null);
    }

    public String getID(String methodName) throws OSGiCoreException {
        return getFeatureValue(methodName, FEATURE_ID, ErrorMessages.NO_METHOD_ID);
    }

    public String getDescription(String methodName) throws OSGiCoreException {
        return getFeatureValue(methodName, FEATURE_DESCRIPTION, ErrorMessages.NO_METHOD_DESCRIPTION);
    }

    private String getFeatureValue(String methodName, String featureName, String exceptionMessage)
            throws OSGiCoreException {

        Map<String, String> features = getFeatures(methodName);
        String featureValue = features.get(featureName).trim();
        if (featureValue.isEmpty()) {
            throw new OSGiCoreException(exceptionMessage, method, declaringClass);
        }
        return featureValue;
    }

    public String[] getKeywords(String methodName) throws OSGiCoreException {
        Map<String, String> features = getFeatures(methodName);
        String[] keywords = features.get(FEATURE_KEYWORDS).trim().split("\\s*" + FEATURE_KEYWORDS_DELIM + "\\s*");
        if (keywords.length == 0) {
            throw new OSGiCoreException(ErrorMessages.NO_METHOD_KEYWORDS, method, declaringClass);
        }
        return keywords;
    }

    private Map<String, String> getFeatures(String methodName) throws OSGiCoreException {
        Map<String, String> features = featureMap.get(methodName);
        if (features == null) {
            throw new OSGiCoreException(ErrorMessages.NO_FEATURE_ANNOTATION, method, declaringClass);
        }
        return features;
    }

    public void validateFeatures(String methodName) throws OSGiCoreException {
        if (!hasFeatures(methodName)) {
            throw new OSGiCoreException(ErrorMessages.NO_FEATURE_ANNOTATION, method, declaringClass);
        }
    }

    private boolean hasFeatures(String methodName) {
        return featureMap.containsKey(methodName);
    }

    public String featureString() {
        return Arrays.toString(featureMap.entrySet().toArray());
    }

}
