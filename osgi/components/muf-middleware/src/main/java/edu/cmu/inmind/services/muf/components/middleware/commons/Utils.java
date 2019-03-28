package edu.cmu.inmind.services.muf.components.middleware.commons;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import edu.cmu.inmind.framework.middleware.data.MwDataMain;
import edu.cmu.inmind.framework.middleware.services.controllers.ServiceExecutor;
import edu.cmu.inmind.multiuser.controller.log.Log4J;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import static edu.cmu.inmind.osgi.apis.Constants.POJO_BUILDER;
import static edu.cmu.inmind.services.muf.components.middleware.commons.Constants.TOKEN;

/**
 * Created by oscarr on 3/1/18.
 */

public class Utils {
    /**
     * this method generates a key that will be used to map services (bundles)
     * using the service name, the method name and the type (alias)
     * @return
     */
    public static String generateMapKey(String bundleId, String bundleAlias, String methodName) throws Exception{
        if( bundleId == null || bundleId.isEmpty() )
            throw new MiddlewareException(ErrorMessages.PARAMETER_CANNOT_BE_NULL, "serviceName");
        if( methodName == null || methodName.isEmpty() )
            throw new MiddlewareException(ErrorMessages.PARAMETER_CANNOT_BE_NULL, "methodName");
        if( bundleAlias == null || bundleAlias.isEmpty() )
            throw new MiddlewareException(ErrorMessages.PARAMETER_CANNOT_BE_NULL, "alias");
        return bundleId + TOKEN + bundleAlias + TOKEN + methodName;
    }



    /**********************************************************************************************/
    /************************************** JSON **************************************************/
    /**********************************************************************************************/
//    build dependency: compile "com.google.code.gson:gson:2.8.2"
    public static <T> String toJson(T object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

//    public interface WritableJsonObject{
//        void clean();
//    }
//
//    private static Gson gsonPretty = new GsonBuilder().setExclusionStrategies(new
//            AnnotationExclusionStrategy()).setPrettyPrinting().create();
//    private static Gson gson = new GsonBuilder().create();
//    Timer timer;
//    static WindowManager windowManager;
//
//    public static <T> String toJsonPretty(T object ){
//        return gsonPretty.toJson(object);
//    }
//
//
//
//    public static <T> T fromJson(String json, Class<T> clazz){
//        if( clazz == DecisionRule.class ){
//            return (T) fromJsonDR(json);
//        }else if( clazz == MicroReaderResultVO.class ){
//            return (T) fromJsonMR(json);
//        }
//        return gsonPretty.fromJson(json, clazz);
//    }
//
//    private static DecisionRule fromJsonDR(String json){
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(PropositionalStatement.class, new DecisionRuleMarshall())
//                .create();
//        DecisionRule decisionRule = gson.fromJson( json, DecisionRule.class );
//        for( DecisionRule.ConditionElement conditionElement : decisionRule.getConditions() ){
//            conditionElement.getProposition().addRule( decisionRule );
//            conditionElement.getProposition().addCondition( conditionElement );
//        }
//        return decisionRule;
//    }
//
//
//    private static MicroReaderResultVO fromJsonMR(String json){
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(MicroReaderSlotValue.class, new MicroReaderSlotDeserializer())
//                .create();
//        MicroReaderResultVO microReaderResultVO = gson.fromJson( json, MicroReaderResultVO.class );
//        return microReaderResultVO;
//    }
//
//    public static <T> T fromJson(String json, Type type){
//        return gsonPretty.fromJson(json, type);
//    }
//
//
//    public static <T> List<T> fromJsonList( String jsonList, Class<T> element ){
//        Type type = new TypeToken<List<T>>() {}.getType();
//        return gsonPretty.fromJson(jsonList, type);
//    }
//
//    public static String toJsonList( List list ){
//        StringBuilder sb = new StringBuilder("[");
//        Field[] fields = null;
//        boolean firstObject = true;
//        for (Object obj : list){
//            if (firstObject){
//                sb.append("{");
//                firstObject = false;
//            }else{
//                sb.append(", {");
//            }
//            if (fields == null){
//                fields = obj.getClass().getFields();
//            }
//            //do sth to retrieve each field value -> json property of json object
//            //add to json array
//            for (int i = 0 ; i < fields.length ; i++){
//                Field f = fields[i];
//                //jsonFromField(sb, obj, i, f);
//            }
//            sb.append("}");
//        }
//        sb.append("]}");
//        return sb.toString();
//    }
//
//
//    /**
//     * This method reads the content of a json reader and creates a corresponding object by
//     * using reflection over a set of mapped values. If the json content contains multiple objects
//     * then a container (List) should be provided.
//     * Use .childString for raw arrays (i.e., String arrays instead of object arrays).
//     * For instance, [0] = "a", [1] = "b"... instead of [name1] = "a", [name2] = "b"...
//     * Use break for breaking the flow when an error ocurrs.
//     * @param reader
//     * @param parentTag
//     * @param mappings
//     * @param result
//     * @param container
//     * @return it returns whether there was an error message during the process
//     * @throws Exception
//     */
//    public static String readJsonToObject(JsonReader reader, String parentTag, HashMap<String,
//            String> mappings, WritableJsonObject result, List container, List<Object> errors) {
//        return readJsonToObject( reader, parentTag, mappings, result, container, errors, -1);
//    }
//
//    /**
//     * This method reads the content of a json reader and creates a corresponding object by
//     * using reflection over a set of mapped values. If the json content contains multiple objects
//     * then a container (List) should be provided.
//     * Use .childString for raw arrays (i.e., String arrays instead of object arrays).
//     * For instance, [0] = "a", [1] = "b"... instead of [name1] = "a", [name2] = "b"...
//     * Use break for breaking the flow when an error ocurrs.
//     * Use .[] to indicate the element is an array and its content should be extracted
//     * use "*" to indicate that this is the base level to add add elements to the container
//     * @param reader
//     * @param parentTag
//     * @param mappings
//     * @param result
//     * @param container
//     * @return it returns whether there was an error message during the process
//     * @throws Exception
//     */
//    private static String readJsonToObject(JsonReader reader, String parentTag, HashMap<String,
//            String> mappings, WritableJsonObject result, List container, List<Object> errors, int hierarchyLevel) {
//
//        String error = null;
//        if( hierarchyLevel >= 0 ) hierarchyLevel++;
//        boolean isArrayWithElements = false;
//        try {
//            JsonToken type = reader.peek();
//            if (type.equals(JsonToken.BEGIN_ARRAY)) {
//                reader.beginArray();
//            } else if (type.equals(JsonToken.BEGIN_OBJECT)) {
//                reader.beginObject();
//            }
//            if (reader.peek().equals(JsonToken.BEGIN_OBJECT)) {
//                do {
//                    boolean isError = validateIsJsonError(readJsonToObject(reader, parentTag, mappings,
//                            result, container, errors, hierarchyLevel), errors);
//                    if (!isError && type.equals(JsonToken.BEGIN_ARRAY) && hierarchyLevel == 1) {
//                        container.add(Util.clone(result));
//                        result.clean();
//                    }
//                } while (type.equals(JsonToken.BEGIN_ARRAY) && reader.hasNext());
//            } else {
//                while (reader.hasNext()) {
//                    String name = reader.peek().equals(JsonToken.NAME) ? reader.nextName()
//                            : parentTag + ".childString";
//                    String smethod = mappings.get(name);
//                    if (smethod == null) {
//                        smethod = mappings.get(parentTag + "." + name);
//                        if (smethod == null) {
//                            smethod = mappings.get(parentTag + ".[]");
//                            if( smethod != null ) {
//                                isArrayWithElements = true;
//                            }
//                        }
//                    }
//                    if (smethod == null) {
//                        reader.skipValue();
//                    } else if (smethod.equals("break")) {
//                        error = reader.nextString();
//                        break;
//                    } else if(smethod.equals("*")){
//                        // from this level and on, we will start adding objects to the container
//                        if( hierarchyLevel == -1 ) {
//                            hierarchyLevel = 0;
//                        }
//                        validateIsJsonError(readJsonToObject(reader, name, mappings, result,
//                                container, errors, hierarchyLevel), errors);
//                    } else if (smethod.equals("")) {
//                        validateIsJsonError(readJsonToObject(reader, name, mappings, result,
//                                container, errors, hierarchyLevel), errors);
//                    } else {
//                        String key = result.getClass().getName() + "." + smethod;
//                        Method method = methodsHash.get( key );
//                        if( method == null ) {
//                            method = result.getClass().getMethod(smethod, String.class);
//                            methodsHash.put( key, method );
//                        }
//                        if( isArrayWithElements ){
//                            method.invoke(result, name + "_:_" + reader.nextString());
//                            isArrayWithElements = false;
//                        }else {
//                            method.invoke(result, reader.nextString());
//                        }
//                    }
//                }
//            }
//            type = reader.peek();
//            if (type.equals(JsonToken.END_ARRAY)) {
//                reader.endArray();
//            } else if (type.equals(JsonToken.END_OBJECT)) {
//                reader.endObject();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return error;
//    }
//
//    private static boolean validateIsJsonError(Object error, List errors){
//        if( error != null ){
//            if( errors == null ){
//                errors =  new ArrayList();
//            }
//            errors.add( error );
//            return true;
//        }
//        return false;
//    }
//
//    public static <T> T readObjectFromJsonFile(String typeStorageDirectory, String fileName,
//                                               Class<T> clazz) {
//        try {
//            File file = new File( Environment.getExternalStoragePublicDirectory(
//                    typeStorageDirectory), fileName + ".json");
//            if( file.exists() ) {
//                String text = new Scanner(file, "UTF-8").useDelimiter("\\A").next();
//                return fromJson(text, clazz);
//            }else{
//                return null;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static <T> T readObjectFromJsonFile(String typeStorageDirectory, String fileName, Type type) {
//        try {
//            File file = new File( Environment.getExternalStoragePublicDirectory(
//                    typeStorageDirectory), fileName + ".json");
//            if( file.exists() ) {
//                String text = new Scanner(file, "UTF-8").useDelimiter("\\A").next();
//                return fromJson(text, type);
//            }else{
//                return null;
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static void writeObjectToJsonFile(Object obj, String typeStorageDirectory, String fileName) {
//        if( obj != null ) {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                try {
//                    File directory = Environment.getExternalStoragePublicDirectory(typeStorageDirectory);
//                    if( !directory.isDirectory() ){
//                        directory.mkdir();
//                    }
//                    File file = new File( directory, fileName + ".json");
//                    PrintWriter writer = new PrintWriter(file, "UTF-8");
//                    writer.print( gsonPretty.toJson( obj ) );
//                    writer.flush();
//                    writer.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    public static <T> String toJsonList( List<T> list ){
//        Type listType = new TypeToken<List<T>>(){}.getComponentName();
//        return gsonPretty.toJsonPretty( list, listType );
//    }

    //    public static JSONArray fromJsonList(String json){
//        JSONArray obj = null;
//        try {
//            obj = (JSONArray) parser.parse(json);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }


    /**********************************************************************************************/
    /************************************* DATES **************************************************/
    /**********************************************************************************************/

    private static final int DEFAULT_TIME_SPAN = 1;  //1 year
    private static String TIME_OFFSET;
    public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    static{
        int offset = ( Calendar.getInstance().get(Calendar.ZONE_OFFSET)
                + Calendar.getInstance().get(Calendar.DST_OFFSET) ) / 3600000;
        String offsetS = "" + offset;
        if( Math.abs( offset ) < 10 ){
            TIME_OFFSET = offsetS.substring(0, 1) + "0" + offsetS.substring(1) + ":00";
        }else{
            TIME_OFFSET = offsetS + ":00";
        }
    }

    public static Calendar getRelativeCalendar(int field, int amount) {
        return getRelativeCalendar(new Date(), field, amount);
    }

    public static Calendar getRelativeCalendar(Date date, int field, int amount){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if( amount > 0 ) {
            cal.add(field, amount);
        }else{
            if( field == Calendar.DAY_OF_YEAR ) {
                cal.add(field, DEFAULT_TIME_SPAN * 365); //1 year
            }else if( field == Calendar.MONTH ) {
                cal.add(field, DEFAULT_TIME_SPAN * 12); //1 years
            }else if( field == Calendar.YEAR ) {
                cal.add(field, DEFAULT_TIME_SPAN); //1 years
            }
        }
        return cal;
    }

    /**
     * Returns a date which is increased a x amount of field (Calendar.DAY_OF_MONTH, Calendar.MONTH, etc)
     * in relation to the current date and time.
     * @param field
     * @param amount
     * @return
     */
    public static Date getRelativeDate(int field, int amount) {
        return getRelativeDate(new Date(), field, amount);
    }

    public static Date getRelativeDate(Date date, int field, int amount){
        return getRelativeCalendar(date, field, amount).getTime();
    }

    public static String getTime(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date) + TIME_OFFSET;
    }

    /**
     * If format is null then "yyyy/MM/dd" will be the default format
     * @param format
     * @return
     */
    public static String getDate(Date date, String format) {
        if( format == null ) format = "yyyy/MM/dd";
        return new SimpleDateFormat( format ).format(date);
    }

    public static Date getOnlyeDate(Date date) {
        if( date == null ) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get( Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get( Calendar.YEAR);
        return getDate(year, month, day);
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month );
        cal.set( Calendar.DAY_OF_MONTH, day );
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set( Calendar.MINUTE, 0);
        cal.set( Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month );
        cal.set( Calendar.DAY_OF_MONTH, day );
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set( Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTime(Date date, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set( Calendar.MINUTE, minute );
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * It returns a full date (date + time)
     * @param date
     * @param time in format HH:MM
     * @return
     */
    public static Date getDateTime(Date date, String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,
                Integer.valueOf(time.substring(0, time.indexOf(":"))));
        calendar.set(Calendar.MINUTE,
                Integer.valueOf(time.substring(time.indexOf(":") + 1)));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * It returns a Date in yyyy/MM/dd format
     * @param miliseconds
     * @return
     */
    public static String formatDate( long miliseconds ){
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(miliseconds));
    }

//    /**
//     * It returns a String (yyyy-MM-dd) or Time (HH:mm) or StringRfc3339 according to "type"
//     * @param date
//     * @param type
//     * @return
//     */
//    public static String formatDate(Date date, String type){
//        if( type.equals("DATE") ) {
//            return new SimpleDateFormat("yyy-MM-dd").format( date );
//        }else if( type.equals("TIME") ){
//            return new SimpleDateFormat("HH:mm").format( date );
//        }
//        return new DateTime( date.getTime() ).toStringRfc3339();
//    }

    public static int getDateField( Date date, int field ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        return calendar.get(field);
    }


    public static boolean isDateInRange( long timeToEvaluate, long threshold, long timeReference ){
        long minRangeTime = timeToEvaluate - threshold/2;
        long maxRangeTime = timeToEvaluate + threshold/2;
        return timeReference >= minRangeTime && timeReference <= maxRangeTime;
    }

    public static Date getDate(String formattedDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);
            if( formattedDate.contains("+") ) {
                TimeZone timezone = TimeZone.getTimeZone("GMT" + formattedDate.substring(formattedDate.indexOf("+")));
                sdf.setTimeZone(timezone);
            }
            return sdf.parse(formattedDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getDateCurrentTimeZone(String stringDate) {
        Date date = getDate( stringDate );
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT" + TIME_OFFSET));
        date = getDate( sdf.format( date ) );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }



    /**********************************************************************************************/
    /************************************** CLONE *************************************************/
    /**********************************************************************************************/

//    private static Cloner cloner = new Cloner();
//
//    public static <T> T clone( T object ){
//        return cloner.deepClone(object);
//    }
//
//    public static <T extends List> T cloneList(T list ){
//        return cloner.deepClone(list);
//    }
//
//    public static ArrayList cloneArray(ArrayList list ){
//        ArrayList result = new ArrayList(list.size());
//        for( Object element : list ){
//            result.add( cloner.deepClone(element) );
//        }
//        return result;
//    }


    /**********************************************************************************************/
    /************************************** XML ***************************************************/
    /**********************************************************************************************/

    /**
     * This method reads the parser's XML content and set the values (specified in the mappings
     * parameter) on the result object by using reflection
     * @param parser
     * @param tag
     * @param mappings
     * @param result
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T readXMLToObject(XmlPullParser parser, String tag, HashMap<String,
                String> mappings, T result) throws Exception {
        parser.require(XmlPullParser.START_TAG, null, tag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String smethod = mappings.get( name );

            //check if there are any attribute to be extracted:
            for( int count = 0; count < parser.getAttributeCount(); count++ ){
                String attribute = parser.getAttributeName( count );
                String methodAtt = mappings.get( name + "." + attribute );
                if( methodAtt != null ){
                    executeMethod( result, methodAtt, false, parser.getAttributeValue(null, attribute) );
                }
            }

            if( smethod == null ){
                skipXMLTag(parser);
            }else if( smethod.equals("") ){
                result = readXMLToObject(parser, name, mappings, result);
            } else {
                Method method = result.getClass().getMethod(smethod, String.class);
                method.invoke(result, parser.nextText());
            }
        }
        return result;
    }

    private static void skipXMLTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


    /**********************************************************************************************/
    /************************************** I/O ***************************************************/
    /**********************************************************************************************/

    // convert inputstream to String
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    /**********************************************************************************************/
    /************************************** IMAGES ************************************************/
    /**********************************************************************************************/

    public static int[] convertYUVtoRGB(byte[] yuv, int width, int height)
            throws NullPointerException, IllegalArgumentException {
        int[] out = new int[width * height];
        int sz = width * height;

        int i, j;
        int Y, Cr = 0, Cb = 0;
        for (j = 0; j < height; j++) {
            int pixPtr = j * width;
            final int jDiv2 = j >> 1;
            for (i = 0; i < width; i++) {
                Y = yuv[pixPtr];
                if (Y < 0)
                    Y += 255;
                if ((i & 0x1) != 1) {
                    final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
                    Cb = yuv[cOff];
                    if (Cb < 0)
                        Cb += 127;
                    else
                        Cb -= 128;
                    Cr = yuv[cOff + 1];
                    if (Cr < 0)
                        Cr += 127;
                    else
                        Cr -= 128;
                }
                int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                if (R < 0)
                    R = 0;
                else if (R > 255)
                    R = 255;
                int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
                        + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                if (G < 0)
                    G = 0;
                else if (G > 255)
                    G = 255;
                int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                if (B < 0)
                    B = 0;
                else if (B > 255)
                    B = 255;
                out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
            }
        }

        return out;
    }


    /**********************************************************************************************/
    /************************************** STRINGS ***********************************************/
    /**********************************************************************************************/


    public static String replaceAll(String str, String pat, String rep){
        if (str == null)
            return null;
        return str.replaceAll(pat, rep);
    }

    public static String listToString( List list ){
        StringBuilder builder = new StringBuilder();
        for( Object obj : list ){
            builder.append( obj.toString() + System.lineSeparator() );
        }
        return builder.toString();
    }

    public static String[] extractIpAddress(String destination){
        String[] destinationArray = new String[3];
        if(destination.contains("rtsp://")) {
            Pattern uri = Pattern.compile("rtsp://(.+):(\\d*)/(.+)");
            Matcher m = uri.matcher(destination);
            m.find();
            destinationArray[0] = m.group(1); //ip
            destinationArray[1] = m.group(2); //port
            destinationArray[2] = m.group(3); //path
        }
        return destinationArray;
    }



    /**********************************************************************************************/
    /************************************** REFLECTION ********************************************/
    /**********************************************************************************************/

    private static LruCache<String, Method> methodsHash = new LruCache<>(500);

    public static <T> T createInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T createInstance(Class<T> clazz, Object... args){
        Class[] parameterTypes = new Class[args.length];
        for( int i = 0; i < args.length; i++ ){
            if( args[i] instanceof Class ){
                parameterTypes[i] = (Class)args[i];
                args[i] = null;
            }else {
                parameterTypes[i] = args[i].getClass();
            }
        }
        try {
            Constructor<T> constructor = clazz.getConstructor( parameterTypes );
            return constructor.newInstance( args );
        }catch (Exception e){
            for( int i = 0; i < args.length; i++ ){
                if( args[i] != null && !(args[i] instanceof Class) ){
                    Class argClass = args[i].getClass().getSuperclass();
                    if( argClass != Object.class && argClass != Class.class ) {
                        parameterTypes[i] = argClass;
                    }
                }
            }
            try{
                Constructor<T> constructor = clazz.getConstructor( parameterTypes );
                return constructor.newInstance( args );
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        return null;
    }

    private static void putMethod(Class clazz, String smethod, Method method ){
        methodsHash.put( clazz.getCanonicalName() + "." + smethod, method);
    }

    private static Method getMethod(Class clazz, String method){
        return methodsHash.get( clazz.getCanonicalName() + "." + method );
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**********************************************************************************************/
    /************************************** CONTROL ***********************************************/
    /**********************************************************************************************/

    private static Random randomNumberGenerator = new Random();
    public static String getRandomNumber(){
        String numberString = new DecimalFormat("00").format(randomNumberGenerator.nextInt(100)).toString();
        // String numberString = new DecimalFormat("0000").format(randomNumberGenerator.nextInt(10000)).toString();
        return numberString;
    }



    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final ListeningExecutorService pool = MoreExecutors.listeningDecorator(executorService);



    /**
     * This method executes ASYNCHRONOUSLY a functionality by enclosing it into a different thread
     * (worker thread) rather than in the main thread (UI thread). You can use lambda expressions, e.g:
     * Util.execute( () -> System.out.println("test") );
     *
     * All Middleware Services have to run their domain methods in threads other than the main
     * thread (UI thread), so use this method to run your logic in a different thread
     *
     * @param //command
     */
//    public static void execute(Runnable command) {
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            if( !pool.isShutdown() ) {
//                pool.submit( command );
//            }
//        } else {
//            command.run();
//        }
//    }

    //from MUF
    public static void execute(Runnable runnable){
        executor.execute(runnable);
    }


    /**
     * This method executes SYNCHRONOUSLY a functionality by enclosing it into a different thread
     * (worker thread) rather than in the main thread (UI thread). You can use lambda expressions, e.g:
     * Util.execute( () -> System.out.println("test") );
     *
     * All Middleware Services have to run their domain (public) methods in threads other than the
     * main thread (UI thread), so use this method to run your logic in a different thread
     *
     * @param command
     * @Nullable
     */
    /*
    public static <T> T executeSync(final Callable<T> command) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if( !pool.isShutdown() ) {
                    return pool.submit(command).get();
                }
                return null;
            } else {
                return command.call();
            }
        }catch(Exception e){
            e.printStackTrace();
            return getReturnableObj(command);
        }
    }
    */

    //TODO: it should return an instance of Future class?
    private static <T> T getReturnableObj(Callable<T> command){
        return null;
    }

    public static void release(){
        executorService.shutdown();
        pool.shutdown();
        try {
            pool.awaitTermination(30, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sleep(long milis) {
        try{
            Thread.yield();
            Thread.sleep( milis );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static void checkValidObject(String type, Object object){
        String name = object.getClass().getCanonicalName() == null? object.getClass().getName()
                : object.getClass().getCanonicalName();
        if( object == null || name.equals("java.lang.Object") )
            throw new NullPointerException( type + " cannot be null nor a plain Object!");
    }

    /**********************************************************************************************/
    /************************************** AWS ***************************************************/
    /**********************************************************************************************/


//    private static boolean transferStatus = false;
//    public static boolean uploadtoAmazonS3(Context context, String poolId, String bucketName, String result, File file)
//    {
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                context,
//                poolId, // Identity Pool ID
//                Regions.US_EAST_1 // Region
//        );
//        AmazonS3 s3client = new AmazonS3Client(credentialsProvider);
//        final TransferUtility transferUtility = new TransferUtility(s3client, context);
//        String fileName = Integer.toString(Math.abs(result.hashCode() + ((int)Math.random() * context.hashCode())));
//        TransferObserver observer = transferUtility.upload("sahilamm",fileName ,file);
//        //Toast.makeText(GeotaggedSurveyService.this, "Sent the data", Toast.LENGTH_SHORT).show();
//        observer.setTransferListener(new TransferListener() {
//            @Override
//            public void onStateChanged(int id, TransferState state) {
//                if(state.equals(TransferState.COMPLETED))
//                {
//                    transferStatus = true;
//                }
//            }
//
//            @Override
//            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                if(bytesCurrent >= bytesTotal)
//                {
//                    transferStatus = true;
//                }
//            }
//
//            @Override
//            public void onError(int id, Exception ex) {
//
//            }
//        });
//        return transferStatus;
//    }


//    class YqlWoeidResponseDeserializer implements JsonDeserializer<YqlWoeidVO> {
//        public YqlWoeidVO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//                throws JsonParseException {
//
//            if (json.getAsJsonObject().get("query").isJsonObject()) {
//                //modify JSON: change results to be an array
//                // ...
//                json = json.getAsJsonObject().getAsJsonArray("place");
//            }
//
//            return new Gson().fromJson(json, YqlWoeidVO.class);
//
//        }
//    }

    private static long time;
    public static void startCrono(){
        time = System.currentTimeMillis();
    }

    public static void stopCrono(String processName) {
        time = System.currentTimeMillis() - time;
        System.out.println(String.format("%s\t%s", processName, time));
        time = -1;
    }

    /**************** Multiuser Framework related client side relevant methods.*******************/
    /***
     *
     * @param address
     * @return
     */
    public static boolean isURLvalid(String address){
        //TODO: we need to replace this with a proper reg exp
        if( address.startsWith("tcp://") ){
            if( !address.contains(":") ){
                address += address + ":" + 5555; //Constants.DEFAULT_PORT;
            }
            String[] ip = address.substring( 6 ).split("\\." );
            for(String segment : ip ){
                if( segment.contains(":") ){
                    segment = segment.split(":")[0];
                }
                int seg = Integer.valueOf(segment);
                if( seg < 0 || seg > 255 ){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**********************************************************************************************/
    /************************************** ZMQ ***************************************************/
    /**********************************************************************************************/


    public static boolean isZMQException(Throwable throwable) {
        String name = throwable.getClass().getPackage().getName();
        if( name.startsWith("java.nio") || name.contains("org.zeromq") || name.contains("zmq") ){
            return true;
        }
        return false;
    }



    public static void print(String tag, String message){
        Log4J.info(tag, message);
        System.out.println(tag + ", " + message);
    }

/**********************************************************************************************/
    /************************************** THREADS ***********************************************/
    /**********************************************************************************************/


    static class ManagableThreadPool extends ThreadPoolExecutor {
        public ManagableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                    new ThreadPoolExecutor.DiscardPolicy());
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r){
            if(r instanceof NamedRunnable) {
                t.setName( ((NamedRunnable) r).getName() );
            }
        }
    }

    public interface NamedRunnable extends Runnable{
        String getName();
    }

    private static ThreadPoolExecutor executor;

    public static Executor getExecutor() {
        return executor;
    }

    private final static int DEFAULT_CORE_POOL_SIZE = 2000;
    /**
     * Reference: {@Link https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html}
     */
    static{
        if( executor == null ) {
            executor = new ManagableThreadPool(
                    DEFAULT_CORE_POOL_SIZE,
                    Integer.MAX_VALUE,
                    5000,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());//(ThreadPoolExecutor) Executors.newCachedThreadPool();
            executor.allowCoreThreadTimeOut(true);
        }
    }


    /**
     * Reference: {@Link http://www.baeldung.com/java-executor-service-tutorial}
     */
    public static void shutdownThreadExecutor(){
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }


    public static <T> Future<T> execute(Callable<T> callable){
        return executor.submit(callable);
    }

    private static Set<Thread> threadSet;
    /**
     * This method prints out the new threads created in comparison with a previous set of threads (threadSet)
     * saved in memory.
     */
    public static void printNewAddedThreads(){
        if( threadSet != null ){
            Set<Thread> threadSetNow = Thread.getAllStackTraces().keySet();
            threadSetNow.removeAll(threadSet);
            for ( Thread t : threadSetNow){
                System.out.println( String.format("Thread: %s state: %s hashcode: %s queue: %s",
                        t, t.getState(), t.hashCode(), executor.getQueue().size() ));
            }
        }
        threadSet = Thread.getAllStackTraces().keySet();
    }

    /**
     * We need to guarantee that once a flag is set to true, it is not undone after that
     * @param flag
     * @param newValue
     */
    public static void setAtom(AtomicBoolean flag, boolean newValue){
        if( !flag.get() && newValue )
            flag.getAndSet( true );
    }


    /**********************************************************************************************/
    /************************************** CLASSPATH SCANNER *************************************/
    /**********************************************************************************************/

    /**
     * Returns a <code>Map<String, Class></code> that holds the value of the specified field,
     * and the class that corresponds to it.
     *
     * @param fieldName the name of the field to obtain while initializing the cache
     * @param superclass the <code>Class</code> whose sub-class the required class is expected to be
     *
     * @return
     */
    public static Map<String, Class> initializeCache(String fieldName, Class superclass) {

        return initializeCache(fieldName, superclass, Boolean.FALSE);
    }

    /**
     * Returns a <code>Map<String, Class></code> that holds the value of the specified field,
     * and the class that corresponds to it.
     *
     * @param fieldName the name of the field to obtain while initializing the cache
     * @param superclass the <code>Class</code> whose sub-class the required class is expected to be
     * @param builderRequired denotes whether the <code>Class</code> is required or its Builder class
     *
     * @return
     */
    public static Map<String, Class> initializeCache(String fieldName, Class superclass,
                                                     boolean builderRequired) {

        // scan through all relevant classes in the package
        List<Class<?>> classes = getDataClasses(superclass);

        // create the cache based on the required field from the class
        Map<String, Class> cache = new HashMap<>();
        for (Class<?> klass : classes) {
            String key = null;
            try {
                key = (String) klass.getField(fieldName).get(null);
                if (builderRequired) {
                    Class builder = (Class) klass.getField(POJO_BUILDER).get(null);
                    cache.put(key, builder);
                }
                else {
                    cache.put(key, klass);
                }

            } catch (NoSuchFieldException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        return cache;
    }

    /**
     * Returns a list of classes that are subclasses of the specified superclass,
     * or, if the specified superclass is an interface, then, it returns the
     * list of classes that implement the interface and are contained within the package.
     *
     * @param desiredClass the <code>Class</code> whose sub-class the required class is expected to be
     * @return
     */
    private static List<Class<?>> getDataClasses(Class desiredClass) {
        String desiredClassName = desiredClass.getSimpleName();
        List<Class<?>> classes = new ArrayList<>();

        String datafile = MwDataMain.FILE_MIDDLEWARE_DATA.getName();
        URL url = Thread.currentThread().getContextClassLoader().getResource(datafile);

        JarFile jarFile = null;
        try {
            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();

            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                if (entry.isDirectory()) continue;
                if (entry.getName().contains(datafile)) {
                    InputStream inputStream = jarFile.getInputStream(entry);
                    Scanner scanner = new Scanner(inputStream);
                    while (scanner.hasNext()) {
                        String line = scanner.nextLine();
                        String[] lineSplit = line.split(MwDataMain.DELIMITER);
                        String superClassName = lineSplit[0];               // name of the superclass
                        String classname = lineSplit[1].substring(6);       // start after "class "
                        if (superClassName.equals(desiredClassName)) {
                            classes.add(loadClass(classname));
                        }
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return classes;
    }

    /**
     * Returns a <code>Class</code> object representing
     * the specified classname.
     *
     * @param classname the name of the class to load
     * @return
     */
    private static Class<?> loadClass (String classname) {
        Class<?> klass = null;
        try {
            klass = Class.forName(classname);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return klass;
    }

}
