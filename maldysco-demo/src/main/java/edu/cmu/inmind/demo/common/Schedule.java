package edu.cmu.inmind.demo.common;

import edu.cmu.inmind.multiuser.controller.common.CommonUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    public volatile static String ALICE = "alice";

    public volatile static long DEFAULT_TIME_DURATION = TimeUnit.DAYS.toMillis(1);

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
        if(userId.equals(OROMERO) || userId.equals(SAKOJU) || userId.equals(ADANGI)
                || userId.equals(ALICE)) return GO_AHEAD;
        TimeSlot timeSlot = schedule.map.get(userId);
        if( timeSlot == null ) {
            //return USER_ID_NOT_EXISTS;
            timeSlot = new TimeSlot(new Date(System.currentTimeMillis()
                    -TimeUnit.MILLISECONDS.toMillis(DEFAULT_TIME_DURATION)),
                    new Date(System.currentTimeMillis()
                            +TimeUnit.MILLISECONDS.toMillis(DEFAULT_TIME_DURATION)));
        }
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
