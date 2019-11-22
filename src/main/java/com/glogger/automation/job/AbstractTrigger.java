package com.glogger.automation.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.glogger.automation.util.DateUtil;

public abstract class AbstractTrigger {
	
	Map<JobDetail, Set<? extends Trigger>> maps = new HashMap<JobDetail, Set<? extends Trigger>>();
	
	public AbstractTrigger(){
		
	}
	
	protected void execute(){
		try{
    		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    		
    		scheduler.start();
    		
    		for(Map.Entry<JobDetail, Set<? extends Trigger>> map : maps.entrySet()){
    			scheduler.scheduleJob(map.getKey(), map.getValue(), true);
    		}
    		
    	}catch (Exception e){
    		e.printStackTrace();
    	}
	}
	
	public void addJob(JobDetail jobDetail, Trigger trigger){
		if (jobDetail == null || trigger == null)
			return; 
					
		if (maps.get(jobDetail) == null){
			Set<Trigger> triggers = new HashSet<Trigger>();
			triggers.add(trigger);
			
			maps.put(jobDetail, triggers);
		}
	}

	protected JobDetail createJob(String key, Class<? extends Job> clazz ){
		JobKey jobKey = new JobKey(key + "Job", "group1");
    	return JobBuilder.newJob(clazz).withIdentity(jobKey).build();
	}
	
	protected CronTrigger createCronTrigger(String key, String schedule, Date startDate, Date endDate){
    	TriggerBuilder<CronTrigger> trigger = TriggerBuilder
		    		.newTrigger()
		    		.withIdentity(key + "Trigger", "group1")
		    		.withSchedule(CronScheduleBuilder.cronSchedule(schedule));
    	
    	if (startDate != null) trigger.startAt(startDate);
    	if (endDate != null) trigger.endAt(endDate);
    	
    	return trigger.build();
    	
	}
	
	protected CronTrigger createCronTrigger(String key, String schedule){
    	return createCronTrigger(key, schedule, null, null);
    	
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
	
	protected SimpleTrigger createSimpleTrigger(String key, Date startDate, Date endDate){
		TriggerBuilder<SimpleTrigger> trigger = TriggerBuilder
		    		.newTrigger()
		    		.withIdentity(key + "Trigger", "group1")
		    		.withSchedule(SimpleScheduleBuilder.simpleSchedule());
    	
    	if (startDate != null) trigger.startAt(startDate);
    	if (endDate != null) trigger.endAt(endDate);
    	
    	return trigger.build();
    	
	}
	
	public Calendar start(int hour){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE, 5);
		
		return calendar;
	}
	
	public Calendar start(Date date_){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date_);
		calendar.add(Calendar.MINUTE, 5);
		
		return calendar;
	}
	
	public Calendar start(Date date_, int seconds){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date_);
		calendar.set(Calendar.SECOND, seconds);
		calendar.add(Calendar.MINUTE, 5);
		
		return calendar;
	}
	
	public Calendar end(Calendar startDate){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate.getTime());
		calendar.add(Calendar.MINUTE, DateUtil.getInstance().getMealDuration() - 10);
		
		return calendar;
	}
	
	public Calendar end(Calendar startDate, int minutes){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate.getTime());
		calendar.add(Calendar.MINUTE, minutes);
		
		return calendar;
	}
}
