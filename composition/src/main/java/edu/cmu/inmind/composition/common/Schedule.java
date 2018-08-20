package edu.cmu.inmind.composition.common;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by oscarr on 4/3/18.
 */
public class Schedule {
    private HashMap<String, TimeSlot> map = new HashMap<>();
    private volatile static Schedule schedule;
    public volatile static String USER_ID_NOT_EXISTS = "USER_ID_NOT_EXISTS";
    public volatile static String TOO_EARLY = "TOO_EARLY";
    public volatile static String TOO_LATE = "TOO_LATE";
    public volatile static String GO_AHEAD = "GO_AHEAD";
    public volatile static String OROMERO = "oromero";
    public volatile static String ADANGI = "adangi";
    public volatile static String SAKOJU = "sakoju";

    static{
        try {
            load();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


    public static void load() throws Throwable{
        schedule = CommonUtils.fromJsonFile("schedule.json", Schedule.class);
    }


    public static String validate(String userId){
        if(userId.equals(OROMERO) || userId.equals(SAKOJU) || userId.equals(ADANGI)) return GO_AHEAD;
        TimeSlot timeSlot = schedule.map.get(userId);
        if( timeSlot == null ) return USER_ID_NOT_EXISTS;
        Date currentTime = new Date(System.currentTimeMillis());
        if( currentTime.before( timeSlot.start) ) return TOO_EARLY;
        if( currentTime.after( timeSlot.end) ) return TOO_LATE;
        return GO_AHEAD;
    }

    static class TimeSlot{
        private Date start;
        private Date end;

        public TimeSlot(Date start, Date end) {
            this.start = start;
            this.end = end;
        }
    }
}
