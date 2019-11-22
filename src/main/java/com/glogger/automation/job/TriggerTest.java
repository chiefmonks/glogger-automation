package com.glogger.automation.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.util.DateUtil;

public class TriggerTest {

    public static void main(String[] args) throws ParseException, SchedulerException {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = JobBuilder.newJob(TestJob.class)
            .withIdentity("cronJob", "testJob") 
            .build();

        String startDateStr = "2013-09-27 00:00:00.0";
        String endDateStr = "2013-09-31 00:00:00.0";

//        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
//        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);
        
		Date mealDate = DateUtil.getInstance().getDate(Constants.FIX_MEAL);
    	Date clockin = DateUtil.getInstance().getDate(Constants.FIX_CLOCKIN);
    	Date login = DateUtil.getInstance().getDate(Constants.FIX_LOGIN);
    	Date clockout = DateUtil.getInstance().getDate(Constants.FIX_LOGOUT);
    	
    	Calendar startDate = start(mealDate, 0);
    	Calendar endDate = end(startDate, 10);
//    	Calendar calendar = GregorianCalendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, 15);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//
//		startDate = calendar;
        
    	//	0 0 12 1/1 * ? *
    	
//    	0 0/1 9-12 * * ?  - run every minutes from 9-12
    	
//*/2 0 9-23 * * ?
//        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
//          .withIdentity("trigger1", "testJob")
//          .startAt(startDate.getTime())
//          .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
//          .endAt(endDate.getTime())
//          .build();
    	
    	System.out.println(startDate.getTime());
    	System.out.println(endDate.getTime());
        
        SimpleTrigger simpleTrigger = createSimpleTrigger("test", 5, startDate.getTime(), endDate.getTime());

        scheduler.scheduleJob(job, simpleTrigger);
        scheduler.start();
    }
    
	protected static SimpleTrigger createSimpleTrigger(String key, int repeatMinutes, Date startDate, Date endDate){
		TriggerBuilder<SimpleTrigger> trigger = TriggerBuilder
		    		.newTrigger()
		    		.withIdentity(key + "Trigger", "group1")
		    		.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(repeatMinutes));
    	
    	if (startDate != null) trigger.startAt(startDate);
    	if (endDate != null) trigger.endAt(endDate);
    	
    	return trigger.build();
    	
	}
    
    public static Calendar start(Date date_){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date_);
		calendar.add(Calendar.MINUTE, 5);
		
		return calendar;
	}
	
	public static Calendar start(Date date_, int seconds){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date_);
		calendar.set(Calendar.SECOND, seconds);
//		calendar.add(Calendar.MINUTE, 5);
		
		return calendar;
	}
	
	public static Calendar end(Calendar startDate){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate.getTime());
		calendar.add(Calendar.MINUTE, DateUtil.getInstance().getMealDuration() - 10);
		
		return calendar;
	}
	
	public static Calendar end(Calendar startDate, int minutes){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate.getTime());
		calendar.add(Calendar.MINUTE, minutes);
//		calendar.set(Calendar.SECOND, minutes);
		
		return calendar;
	}

    public static class TestJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("this is a cron scheduled test job");
        }        
    }
}
