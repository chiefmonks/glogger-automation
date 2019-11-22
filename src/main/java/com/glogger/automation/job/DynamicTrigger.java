package com.glogger.automation.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.glogger.automation.util.DateUtil;

public class DynamicTrigger extends AbstractTrigger{
	
	Map<JobDetail, Set<? extends Trigger>> map = new HashMap<JobDetail, Set<? extends Trigger>>();

	public DynamicTrigger() {
		
		normalJobs();
		
		monitorJobs();
    	
    	execute();
	}
	
	private void normalJobs(){
		Date mealDate = DateUtil.getInstance().getMealDate();
    	Date mealOutDate = DateUtil.getInstance().getMealOutDate();
    	
    	JobDetail loginJob = createJob("login", LoginJob.class);
    	
    	//login trigger
    	SimpleTrigger loginTrigger = createSimpleTrigger("login", null, null);

    	
    	JobDetail mealJob = createJob("meal", MealJob.class);
    	//meal trigger
    	SimpleTrigger mealTrigger = createSimpleTrigger("meal", mealDate, null);
    	
    	JobDetail clockInJob = createJob("clockIn", ClockInJob.class);
    	//clock in trigger
    	SimpleTrigger clockInTrigger = createSimpleTrigger("clockIn", mealOutDate, null);
    	
    	JobDetail clockOutJob = createJob("clockOut", ClockOutJob.class);
    	
    	//clockout trigger
    	SimpleTrigger clockOutTrigger = TriggerBuilder
		    		.newTrigger()
		    		.withIdentity("clockOutTrigger", "group1")
		    		.withSchedule(SimpleScheduleBuilder.simpleSchedule())
		    		.startAt(DateUtil.getInstance().getEndDate())
		    		.build();

    	
    	addJob(loginJob, loginTrigger);
    	addJob(mealJob, mealTrigger);
    	addJob(clockInJob, clockInTrigger);
    	addJob(clockOutJob, clockOutTrigger);
	}
	
	private void monitorJobs(){
		Date mealDate = DateUtil.getInstance().getMealDate();
    	Date mealOutDate = DateUtil.getInstance().getMealOutDate();
    	
    	Calendar startDate = start(mealDate);
    	Calendar endDate = end(startDate, 10);
		
		JobDetail mealMonitorJob = createJob("mealMonitor", MealMonitorJob.class);
    	//meal monitor trigger
//    	CronTrigger mealMonitorTrigger= createCronTrigger("mealMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger mealMonitorTrigger = createSimpleTrigger("mealMonitor", 5, startDate.getTime(), endDate.getTime());
    	
    	Calendar clockInDate = GregorianCalendar.getInstance();
    	clockInDate.setTime(mealOutDate);
    	
    	startDate = start(clockInDate.getTime());
    	endDate = end(startDate, 10);
    	
		JobDetail clockInMonitorJob = createJob("clockInMonitor", ClockInMonitorJob.class);
    	//clock in monitor trigger
//    	CronTrigger clockInMonitorTrigger= createCronTrigger("clockInMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger clockInMonitorTrigger = createSimpleTrigger("clockInMonitor", 5, startDate.getTime(), endDate.getTime());

		startDate = start(DateUtil.getInstance().getStartDate());
		endDate = end(startDate, 10);

		JobDetail loginMonitorJob = createJob("loginMonitor", LoginMonitorJob.class);
    	//log in monitor trigger
//    	CronTrigger loginMonitorTrigger= createCronTrigger("loginMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger loginMonitorTrigger = createSimpleTrigger("loginMonitor", 5, startDate.getTime(), endDate.getTime());
    	
		startDate = start(DateUtil.getInstance().getEndDate());
		endDate = end(startDate, 10);

		JobDetail clockOutMonitorJob = createJob("clockOutMonitor", ClockOutMonitorJob.class);
    	//log in monitor trigger
//    	CronTrigger clockOutMonitorTrigger= createCronTrigger("clockOutMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger clockOutMonitorTrigger = createSimpleTrigger("clockOutMonitor", 5, startDate.getTime(), endDate.getTime());

    	addJob(mealMonitorJob, mealMonitorTrigger);
		addJob(clockInMonitorJob, clockInMonitorTrigger);
		addJob(loginMonitorJob, loginMonitorTrigger);
		addJob(clockOutMonitorJob, clockOutMonitorTrigger);
    	
	}
	
	public static void main(String[] args) {
		new DynamicTrigger();
	}
	
}
