package edu.cmu.inmind.services.muf.components.services.registry;

import edu.cmu.inmind.osgi.commons.core.BundleApiInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfo;
import edu.cmu.inmind.osgi.commons.core.BundleImplInfoBuilder;
import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.markers.BundleImpl;
import edu.cmu.inmind.osgi.commons.markers.Feature;
import edu.cmu.inmind.osgi.commons.utils.AnnotationParser;
import edu.cmu.inmind.osgi.commons.utils.CommonUtils;
import edu.cmu.inmind.osgi.commons.utils.ErrorMessages;
import edu.cmu.inmind.osgi.commons.utils.OSGiCoreException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class BundleMethodExtractor {

    public static BundleImplInfo extract(Object bundleInstance, List<String> ignoreableServices) throws Exception {
        BundleImplInfoBuilder bundleBuilder = new BundleImplInfoBuilder();

        // instance id and instance api
        extractBundleContracts(bundleBuilder, bundleInstance, ignoreableServices);

        if (!SRCommonUtils.isAnnotationPresent(bundleBuilder.getClassType(), BundleImpl.class)) {
            throw new OSGiCoreException(ErrorMessages.NO_BUNDLE_TYPE, bundleBuilder.getClassType());
        }

        // let's get the instance alias
        extractBundleType(bundleBuilder);

        // the instance implementation should define public methods
        extractFeatures(bundleBuilder);

        return bundleBuilder.build();
    }

    private static void extractBundleContracts(BundleImplInfoBuilder bundleBuilder,
                                               Object bundleInstance,
                                               List<String> ignoreableServices) throws Exception {

        Class bundle = bundleInstance.getClass();
        bundleBuilder.setClassType(bundle);
        bundleBuilder.setInstance(bundleInstance);

        List<Class> interfaces = extractInterfaces(bundle, ignoreableServices);
        for (Class api : interfaces) {
            assertBundleApiAnnotation(api);

            Annotation[] annotations = api.getAnnotations();
            for (Annotation annotation : annotations) {
                if (AnnotationParser.isAnnotationType(annotation, BundleAPI.class)) {
                    String id = (String) AnnotationParser.getMethodValue(annotation, "id");
                    if (id == null || id.isEmpty()) {
                        throw new OSGiCoreException(ErrorMessages.NO_BUNDLE_ID, api);
                    }
                    else {
                        bundleBuilder.setId(id);
                        bundleBuilder.addBundleAPI(api);
                    }
                }
            }
        }

        if (bundleBuilder.getId() == null) {
            throw new OSGiCoreException(ErrorMessages.NO_SERVICE_NAME, bundle);
        }
    }

    private static void extractBundleType(BundleImplInfoBuilder bundleBuilder) throws Exception {
        Class bundleImplClass = bundleBuilder.getClassType();
        Annotation[] annotations = bundleImplClass.getAnnotations();

        for (Annotation annotation : annotations) {
            if (AnnotationParser.isAnnotationType(annotation, BundleImpl.class)) {
                String alias = (String) AnnotationParser.getMethodValue(annotation, "alias");
                if (alias == null || alias.isEmpty()) {
                    throw new OSGiCoreException(ErrorMessages.EMPTY_BUNDLE_TYPE, bundleImplClass);
                }
                else {
                    bundleBuilder.setAlias(alias);
                }
            }
        }
    }

    private static void extractFeatures(BundleImplInfoBuilder builder) throws Exception {
        for (Class bundleAPI : builder.getApis().keySet()) {
            Class bundle = builder.getClassType();

            List<Method> methods = CommonUtils.getPublicMethods(bundleAPI);
            if (methods == null || methods.isEmpty()) {
                throw new OSGiCoreException(ErrorMessages.NO_BUNDLE_METHODS, bundleAPI);
            }
            for (Method method : methods) {
                Annotation featureAnnotation = SRCommonUtils.getAnnotation(method, Feature.class);
                if (featureAnnotation == null) {
                    throw new OSGiCoreException(ErrorMessages.NO_FEATURE_ANNOTATION, method, bundleAPI);
                }

                String id = (String) AnnotationParser.getMethodValue(featureAnnotation, "id");
                String description = (String) AnnotationParser.getMethodValue(featureAnnotation, "description");
                String[] keywords = (String[]) AnnotationParser.getMethodValue(featureAnnotation, "keywords");

                if (id.isEmpty()) {
                    throw new OSGiCoreException(ErrorMessages.NO_METHOD_ID, method, bundle);
                }
                if (description.isEmpty()) {
                    throw new OSGiCoreException(ErrorMessages.NO_METHOD_DESCRIPTION, method, bundle);
                }
                if (keywords.length == 0) {
                    throw new OSGiCoreException(ErrorMessages.NO_METHOD_KEYWORDS, method, bundle);
                }

                BundleApiInfo bundleApiInfo = new BundleApiInfo();
                bundleApiInfo.setKeywords(keywords);
                bundleApiInfo.setMethod(method);
                bundleApiInfo.setParameters(method.getParameterTypes());

                builder.addBundleApiInfo(bundleAPI, bundleApiInfo);
            }
        }
    }

    private static List<Class> extractInterfaces(Class bundle, List<String> ignoreableServices) throws Exception {
        List<Class> interfaces = SRCommonUtils.getAllInterfaces(bundle, ignoreableServices);
        if (interfaces.size() == 0) {
            throw new OSGiCoreException(ErrorMessages.NO_APIS, bundle);
        }
        return interfaces;
    }

    private static void assertBundleApiAnnotation(Class api) throws OSGiCoreException {
        if (!SRCommonUtils.isAnnotationPresent(api, BundleAPI.class)) {
            throw new OSGiCoreException(ErrorMessages.NO_BUNDLE_API_ANNOTATION, api);
        }
    }

}
