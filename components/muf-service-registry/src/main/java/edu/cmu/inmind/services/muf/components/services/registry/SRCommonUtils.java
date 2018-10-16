package edu.cmu.inmind.services.muf.components.services.registry;

import edu.cmu.inmind.osgi.commons.core.GenericBundle;
import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.utils.ErrorMessages;
import edu.cmu.inmind.osgi.commons.utils.OSGiCoreException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SRCommonUtils {

    public static boolean isAnnotationPresent(Class clazz, Class annotationClazz) {
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations == null || annotationClazz == null) {
            return Boolean.FALSE;
        }

        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals(annotationClazz.getSimpleName())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Annotation getAnnotation(Method method, Class annotationClazz) {
        Annotation[] annotations = method.getAnnotations();
        if (annotations == null || annotationClazz == null) {
            return null;
        }

        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals(annotationClazz.getSimpleName())) {
                return annotation;
            }
        }
        return null;
    }

    public static List<Class> getAllInterfaces(Class<?> clazz, List<String> ignoreableServices) throws Exception {
        List<Class> interfaces = new ArrayList();
        addAllInterfaces(interfaces, clazz, ignoreableServices);
        return interfaces;
    }

    private static boolean addAllInterfaces(List<Class> list, Class<?> clazz, List<String> ignoreableServices) throws Exception {
        Class[] var3 = clazz.getInterfaces();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Class<?> c = var3[var5];
            if (!isCoreJavaAPIs(c) && !serviceIgnoreable(c.getName(), ignoreableServices)) {
                if (isAnnotationPresent(c, BundleAPI.class)) {
                    list.add(c);
                }

                // the first condition is because we introduced GenericBundle, we may remove it when we remove the GenericBundle class
                if (!c.getName().equals(GenericBundle.class.getName())
                        && addAllInterfaces(list, c, ignoreableServices)
                        && clazz.getAnnotation(BundleAPI.class) == null     // && !isAnnotationPresent(clazz, BundleAPI.class)
                        && clazz.isInterface()) {
                    throw new OSGiCoreException(ErrorMessages.NO_BUNDLE_API_ANNOTATION, new Object[]{clazz});
                }
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (validSuperClass(superClass)) {
            addAllInterfaces(list, superClass, ignoreableServices);
        }

        return list.contains(clazz);
    }

    private static boolean validSuperClass(Class<?> superClass) {
        return superClass != null && superClass != Object.class;
    }

    public static boolean isCoreJavaAPIs(Class clazz) {
        return clazz.getName().startsWith("java.");
    }

    public static boolean serviceIgnoreable(String serviceName, List<String> ignoreableServices) {
        if (ignoreableServices == null) {
            return Boolean.FALSE;
        } else {
            Iterator var2 = ignoreableServices.iterator();

            String ignoreableService;
            do {
                if (!var2.hasNext()) {
                    return Boolean.FALSE;
                }

                ignoreableService = (String)var2.next();
            } while(!serviceIgnoreable(serviceName, ignoreableService));

            return Boolean.TRUE;
        }
    }

    private static boolean serviceIgnoreable(String serviceName, String ignoreableService) {
        return serviceName.startsWith(ignoreableService);
    }
}
