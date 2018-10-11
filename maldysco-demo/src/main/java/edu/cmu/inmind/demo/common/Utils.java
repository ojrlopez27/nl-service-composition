package edu.cmu.inmind.demo.common;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created for demo : sakoju 10/4/2018
 */
public class Utils {

    public static String getSystemIPAddress() {
        String ipAddress = "";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String splitByCapitalizedWord(String word, boolean lowercase){
        String[] split = word.split("(?=\\p{Upper})");
        String newWord = "";
        for(String sp : split){
            newWord += (!newWord.isEmpty()? "-" + sp : sp);
        }
        return lowercase? newWord.toLowerCase() : newWord;
    }

    public static String removeEOS(String original){
        return original.replace(DemoConstants.EOS, "")
                .replace("\\u003ceos\\git au003e", "");
    }


    public static String removeExtraQuotes(String payload){
        payload = payload.trim();
        if( payload.startsWith("\"") ) payload = payload.substring(1);
        if( payload.endsWith("\"") ) payload = payload.substring(0, payload.length() - 1);
        return payload;
    }

    private static long time;
    public static void startChrono(){
        time = System.currentTimeMillis();
    }

    public static long stopChrono(){
        return (System.currentTimeMillis() - time);
    }
    private static int ruleCont = 1;
    public static String getRuleName(String step) {
        String[] words = step.split(" ");
        if(words.length > 0){
            String name = "";
            for(String word : words){
                name += name.isEmpty()? word : "-" + word;
            }
            return String.format("Rule%s-%s", (ruleCont++), name);
        }
        return String.format("Rule%s-%s", (ruleCont++), step);
    }

    //TODO: remove all services, methods, pojos and instead just add strings for testing
    private static Map<String, ServiceMethod> mapInterfaces, mapImplementations;
    public static Map<String, ServiceMethod> generateCorporaFromMethods(boolean isMechanicalTurkTest){
        mapInterfaces = extractClassesFromPackage(GenericService.class.getPackage().getName(),
                CommonUtils.getProperty("sent2vec.corpora.methods.path"), true,
                isMechanicalTurkTest);
        extractImplementationClasses();
        return mapInterfaces;
    }
    //TODO: remove all services, methods, pojos and instead just add strings for testing

    public static Map<String, ServiceMethod> extractImplementationClasses(){
        mapImplementations = extractClassesFromPackage(AirBnBService.class.getPackage().getName(),null,
                false, false);
        return mapImplementations;
    }

    private static Map<String, ServiceMethod> extractClassesFromPackage(String packageName, String path,
                                                                        boolean checkDescriptionAnnotation,
                                                                        boolean isMechanicalTurkTest){
        // loading all the classes from 'services' package
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
        Set<Class<?>> services = reflections.getSubTypesOf(Object.class);

        // iterate over the classes and methods, and then extract their description
        Map map = new HashMap<>();
        StringBuffer corpora = new StringBuffer();
        for(Class service : services){
            for(Method method : service.getDeclaredMethods()){
                if( checkDescriptionAnnotation ) {
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        if (annotation instanceof Description) {
                            for (String capability : ((Description) annotation).capabilities()) {
                                appendToStrBuffer(corpora, capability);
                                map.put(capability.toLowerCase(), new ServiceMethod(service, method,
                                        method.getGenericParameterTypes()));
                            }
                        }
                    }
                }else{
                    map.put(service.getName()+"."+method.getName(),
                            new ServiceMethod(service, method, method.getGenericParameterTypes()));
                }
            }
        }
        // store the corpora file
        if(path != null){
            if(isMechanicalTurkTest)
                corpora.append("\n").append(DatasetCleaner.getCleanDataset());
            printToFile(path, corpora);
        }
        return map;
    }

    public static <A> Map<String, A> asMap(Object... keysAndValues) {
        return new LinkedHashMap<String, A>() {{
            for (int i = 0; i < keysAndValues.length - 1; i++) {
                put(keysAndValues[i].toString(), (A) keysAndValues[++i]);
            }
        }};
    }


}
