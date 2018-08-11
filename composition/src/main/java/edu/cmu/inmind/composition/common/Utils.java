package edu.cmu.inmind.composition.common;

import edu.cmu.inmind.composition.annotations.BatteryQoS;
import edu.cmu.inmind.composition.annotations.ConnectivityQoS;
import edu.cmu.inmind.composition.annotations.Description;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.services.AirBnBService;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by oscarr on 8/9/18.
 */
public class Utils {
    private static String lineSeparator = System.lineSeparator();

    public static void generateInputText(CompositionController.CompositeService compositeService){
        // extract steps from composite service
        StringBuffer inputText = new StringBuffer();
        for(Node node : compositeService.getNodes()){
            if( !node.getType().equals(Node.NodeType.IF) ){
                appendToStrBuffer(inputText, node.getName());
            }else{
                for(Node childNode : node.getChildren()){
                    appendToStrBuffer(inputText, childNode.getName());
                }
            }
        }

        // store the corpora file
        printToFile("/Users/oscarr/Development/sent2vec/inputText", inputText);
    }


    private static Map<String, ServiceMethod> mapInterfaces, mapImplementations;
    public static Map<String, ServiceMethod> generateCorporaFromMethods(){
        mapInterfaces = extractClassesFromPackage(GenericService.class.getPackage().getName(),
                "/Users/oscarr/Development/sent2vec/corporaMethods", true);
        extractImplementationClasses();
        return mapInterfaces;
    }

    public static Map<String, ServiceMethod> extractImplementationClasses(){
        mapImplementations = extractClassesFromPackage(AirBnBService.class.getPackage().getName(),null, false);
        return mapImplementations;
    }

    private static Map<String, ServiceMethod> extractClassesFromPackage(String packageName, String path,
                                                                        boolean checkDescriptionAnnotation){
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
            printToFile(path, corpora);
        }
        return map;
    }

    public static List<ServiceMethod> getImplementationsOf(Class<? extends GenericService> interfaceClass, Method method){
        List<ServiceMethod> implementations = new ArrayList<>();
        for(ServiceMethod serviceMethod : mapImplementations.values()){
            if( serviceMethod.getServiceMethod().getName().equals(method.getName()) ) {
                for (Class implInterface : serviceMethod.getServiceClass().getInterfaces()) {
                    if (implInterface == interfaceClass && !implementations.contains(serviceMethod.getServiceClass())) {
                        implementations.add(serviceMethod);
                    }
                }
            }
        }
        return implementations;
    }


    private static void appendToStrBuffer(StringBuffer buffer, String line){
        if( buffer.length() > 0 ) buffer.append( lineSeparator );
        buffer.append(line);
    }

    private static void printToFile(String filename, StringBuffer buffer){
        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            pw.print(buffer);
            pw.flush();
            pw.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }


    private static List<String> readFromFile(String filename){
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.substring( line.indexOf(" ") + 1);
                line = line.replace(" , ", ", ");
                lines.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> getAbstractServices() {
        return readFromFile("/Users/oscarr/Development/sent2vec/outputSent2Vec.txt");
    }


    public static Object[] convertToParams(Type[] types){
        Object[] params = new Object[types.length];
        try {
            for (int i = 0; i < types.length; i++) {
                if( types[i] == Double.class){
                    params[i] = new Double(0);
                }else {
                    params[i] = ((Class) types[i]).newInstance();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return params;
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

    public static ServiceMethod selectService(String qoSfeature, List<ServiceMethod> implementations){
        String QoSValue = "";
        if( qoSfeature.equals(Constants.LOW_BATTERY_SERVICE) ) QoSValue = Constants.WORKS_WITH_LOW_CHARGE;
        else if( qoSfeature.equals(Constants.HIGH_BATTERY_SERVICE) ) QoSValue = Constants.REQUIRES_FULLY_CHARGED;
        else if( qoSfeature.equals(Constants.PICK_REMOTE_SERVICE) ) QoSValue = Constants.REQUIRES_WIFI_CONNECTIVITY;
        else if( qoSfeature.equals(Constants.PICK_LOCAL_SERVICE) ) QoSValue = Constants.NOT_REQUIRES_WIFI_CONNECTIVITY;

        for(ServiceMethod implementation : implementations){
            Method method = implementation.getServiceMethod();
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof BatteryQoS
                        && ((BatteryQoS) annotation).minBatteryLevel().equals(QoSValue)) {
                    return implementation;
                } else if (annotation instanceof ConnectivityQoS
                        && ((ConnectivityQoS) annotation).wifiStatus().equals(QoSValue)) {
                    return implementation;
                }
            }
        }
        Random random = new Random();
        int idx = random.nextInt(implementations.size());
        return implementations.get(idx);
    }

    public static Object executeMethod(ServiceMethod serviceMethod) {
        try {
            Constructor<?> constructor = serviceMethod.getServiceClass().getConstructor();
            Object service = constructor.newInstance();
            return serviceMethod.getServiceMethod().invoke(service, Utils.convertToParams(serviceMethod.getParams()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
