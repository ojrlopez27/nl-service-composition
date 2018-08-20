package edu.cmu.inmind.composition.common;

import edu.cmu.inmind.composition.annotations.*;
import edu.cmu.inmind.composition.controllers.CommunicationController;
import edu.cmu.inmind.composition.controllers.CompositionController;
import edu.cmu.inmind.composition.apis.GenericService;
import edu.cmu.inmind.composition.controllers.DatasetCleaner;
import edu.cmu.inmind.composition.pojos.AbstractServicePOJO;
import edu.cmu.inmind.composition.pojos.LocationPOJO;
import edu.cmu.inmind.composition.pojos.NERPojo;
import edu.cmu.inmind.composition.services.AirBnBService;
import edu.cmu.inmind.multiuser.controller.common.CommonUtils;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
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
import java.text.SimpleDateFormat;
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
        printToFile(CommonUtils.getProperty("sent2vec.input.text.path"), inputText);
    }


    private static Map<String, ServiceMethod> mapInterfaces, mapImplementations;
    public static Map<String, ServiceMethod> generateCorporaFromMethods(boolean isMechanicalTurkTest){
        mapInterfaces = extractClassesFromPackage(GenericService.class.getPackage().getName(),
                CommonUtils.getProperty("sent2vec.corpora.methods.path"), true,
                isMechanicalTurkTest);
        extractImplementationClasses();
        return mapInterfaces;
    }

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

    public static void printToFile(String filename, StringBuffer buffer){
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
        return readFromFile(CommonUtils.getProperty("sent2vec.semantic.similarity.path"));
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

    public static Object executeMethod(ServiceMethod serviceMethod, Object[] args) {
        try {
            Constructor<?> constructor = serviceMethod.getServiceClass().getConstructor();
            Object service = constructor.newInstance();
            return args == null? serviceMethod.getServiceMethod().invoke(service) :
                    serviceMethod.getServiceMethod().invoke(service, args) ; //Utils.convertToParams(serviceMethod.getParams())
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object[] matchEntitiesToArgs(List<NERPojo> entities, Method method, Type[] params) {
        Object[] args = params == null || params.length == 0? null : new Object[params.length];
        if(args != null){
            for(int i = 0; i < args.length; i++){
                NERPojo toRemove = null;
                String value = getValueProvidedAnnotation(method, i);
                if(value != null){
                    args[i] = getObject(params[i], value, null);
                }else {
                    for (NERPojo nerPojo : entities) {
                        args[i] = getObject(params[i], nerPojo.getNormalizedAnnotation(), nerPojo);
                        if (args[i] != null) {
                            toRemove = nerPojo;
                            break;
                        }
                    }
                    if (args[i] != null) entities.remove(toRemove);
                }
            }
        }
        return args;
    }


    private static String getValueProvidedAnnotation(Method method, int idx){
        for(Annotation annotation : method.getParameterAnnotations()[idx] ){
            if(annotation instanceof Provided){
                return ((Provided)annotation).value();
            }
        }
        return null;
    }

    private static Object getObject(Type param, String value, NERPojo nerPojo) {
        Object arg = null;
        if( (param.equals(Short.class) || param.equals(Integer.class) || param.equals(Long.class) || param.equals(Double.class))
                && (nerPojo == null || (nerPojo.getAnnotation().equals("NUMBER")
                || nerPojo.getAnnotation().equals("ORDINAL") || nerPojo.getAnnotation().equals("MONEY")))){
            arg = Double.parseDouble(value);
        }else if(param.equals(Date.class) && (nerPojo == null || (nerPojo.getAnnotation().equals("DATE")
                || nerPojo.getAnnotation().equals("TIME")))){
            arg = nerPojo != null? nerPojo.getDate() : getDate(value);
        }else if(param.equals(String.class) && (nerPojo == null || (nerPojo.getAnnotation().equals("PERSON")
                || nerPojo.getAnnotation().equals("ORGANIZATION")))){
            arg = value;
        }else if(param.equals(LocationPOJO.class) && (nerPojo == null || nerPojo.getAnnotation().equals("LOCATION"))){
            arg = new LocationPOJO(nerPojo != null? nerPojo.getWord() : value);
        }
        return arg;
    }

    private static Date getDate(String value) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getArgDescAnnotation(Method method, int idx) {
        Class[] interfaces = method.getDeclaringClass().getInterfaces();
        Method targetMethod = null;
        for(Class interfaceClass : interfaces){
            for(Method interfaceMethod : interfaceClass.getDeclaredMethods()){
                if(interfaceMethod.getName().equals(method.getName())){
                    targetMethod = interfaceMethod;
                    break;
                }
            }
            if(targetMethod != null) break;
        }

        for (Annotation annotation : targetMethod.getAnnotations()) {
            if (annotation instanceof ArgDesc) {
                return ((ArgDesc)annotation).args()[idx].split(" : ");
            }
        }
        return null;
    }

    public static Object getObjectFromAnswer(String answer, Type type) {
        return getObject(type, answer, null);
    }

    public static List<AbstractServicePOJO> extractAbstractServices(String abstracServiceCandidates) {
        int begin = 0;
        for(char charAtPos : abstracServiceCandidates.toCharArray()){
            if(charAtPos != 65533) break;
            begin++;
        }
        List<AbstractServicePOJO> list = new ArrayList<>();
        abstracServiceCandidates = abstracServiceCandidates.substring(begin);
        for(String candidate : abstracServiceCandidates.split("\n")){
            String[] elements = candidate.split("@@");
            list.add(new AbstractServicePOJO(Double.parseDouble(elements[0]),
                    elements[1].substring(elements[1].indexOf(" ")+1) ));
        }
        return list;
    }

    private static long time;
    public static void startChrono(){
        time = System.currentTimeMillis();
    }

    public static long stopChrono(){
        return (System.currentTimeMillis() - time);
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
        return original.replace(Constants.EOS, "")
                .replace("\\u003ceos\\git au003e", "");
    }


    public static String removeExtraQuotes(String payload){
        payload = payload.trim();
        if( payload.startsWith("\"") ) payload = payload.substring(1);
        if( payload.endsWith("\"") ) payload = payload.substring(0, payload.length() - 1);
        return payload;
    }

    public static String removeAllQuotes(String payload){
        return payload.replace("\"", "").replace("\'", "").trim();
    }

    public static String extractCleanPayload(String msg) {
        int start = msg.indexOf("\'payload");
        if( start == -1 ) start = msg.indexOf("\"payload");
        int end = msg.indexOf("\'messageId");
        if( end == -1 ) end = msg.indexOf("\"messageId");
        String payload = msg.substring( start, end );
        payload = payload.replace("u\"", "\"");
        if(payload.contains("\'")) {
            final String token = "@@@@";
            for (start = payload.indexOf("\""), end = 0; start < payload.length() && end < payload.length(); ) {
                end = payload.indexOf("\"", start + 1);
                if (start == -1 || end == -1) break;
                String substring = payload.substring(start, end + 1);
                substring = substring.replace("\'", token);
                //            if(substring.startsWith("\"")) substring = substring.substring(1);
                //            if(substring.endsWith("\"")) substring = substring.substring(0, substring.length() - 1);
                payload = payload.replace(payload.substring(start, end + 1), substring);
                start = end + 1;
            }
            payload = payload.replace("\'", "\"");
            payload = payload.replace(token, "\'");
        }
        payload = (payload.substring(0, payload.lastIndexOf(","))
                .replace("\"{", "{")
                .replace("}\"", "}"))
                .replace("\\", "")
                .replace("\"payload\":", "");
        return payload;
    }
}
