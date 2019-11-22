package com.glogger.automation.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.util.DateUtil;

public class FixTimeTrigger extends AbstractTrigger{
	
	Map<JobDetail, Set<? extends Trigger>> map = new HashMap<JobDetail, Set<? extends Trigger>>();

	public FixTimeTrigger() {
		
		normalJobs();
		
		monitorJobs();
    	
    	execute();
	}
	
	private void normalJobs(){
    	JobDetail loginJob = createJob("login", LoginJob.class);
    	
    	int[] time = getValue(Constants.FIX_LOGIN);
    	//login trigger
    	CronTrigger loginTrigger = createCronTrigger("login", String.format("0 %d %d 1/1 * ? *", time[1], time[0]));
    	
    	
    	JobDetail mealJob = createJob("meal", MealJob.class);
    	time = getValue(Constants.FIX_MEAL);
    	
    	//meal trigger
    	CronTrigger mealTrigger = createCronTrigger("meal", String.format("0 %d %d 1/1 * ? *", time[1], time[0]));
    	
    	JobDetail clockInJob = createJob("clockIn", ClockInJob.class);
    	time = getValue(Constants.FIX_CLOCKIN);
    	
    	//clock in trigger
    	CronTrigger clockInTrigger = createCronTrigger("clockIn", String.format("0 %d %d 1/1 * ? *", time[1], time[0]));
    	
    	JobDetail clockOutJob = createJob("clockOut", ClockOutJob.class);
    	time = getValue(Constants.FIX_LOGOUT);
    	int buffer = Integer.valueOf(Constants.TIME_BUFFER);
    	
    	//clockout trigger
    	CronTrigger clockOutTrigger = createCronTrigger("clockOut", String.format("0 %d %d 1/1 * ? *", time[1] + buffer, time[0]));

    	
    	addJob(loginJob, loginTrigger);
    	addJob(mealJob, mealTrigger);
    	addJob(clockInJob, clockInTrigger);
    	addJob(clockOutJob, clockOutTrigger);
	}
	
	private void monitorJobs(){
		Date mealDate = DateUtil.getInstance().getDate(Constants.FIX_MEAL);
    	Date clockin = DateUtil.getInstance().getDate(Constants.FIX_CLOCKIN);
    	Date login = DateUtil.getInstance().getDate(Constants.FIX_LOGIN);
    	Date clockout = DateUtil.getInstance().getDate(Constants.FIX_LOGOUT);
    	
    	Calendar startDate = start(mealDate, 0);
    	Calendar endDate = end(startDate, 10);
		
		JobDetail mealMonitorJob = createJob("mealMonitor", MealMonitorJob.class);
    	//meal monitor trigger
//    	CronTrigger mealMonitorTrigger= createCronTrigger("mealMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger mealMonitorTrigger = createSimpleTrigger("mealMonitor", 5, startDate.getTime(), endDate.getTime());
    	
    	
    	startDate = start(clockin, 0);
    	endDate = end(startDate, 10);
    	
		JobDetail clockInMonitorJob = createJob("clockInMonitor", ClockInMonitorJob.class);
    	//clock in monitor trigger
//    	CronTrigger clockInMonitorTrigger= createCronTrigger("clockInMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger clockInMonitorTrigger = createSimpleTrigger("clockInMonitor", 5, startDate.getTime(), endDate.getTime());

		startDate = start(login, 0);
		endDate = end(startDate, 10);

		JobDetail loginMonitorJob = createJob("loginMonitor", LoginMonitorJob.class);
    	//log in monitor trigger
//    	CronTrigger loginMonitorTrigger= createCronTrigger("loginMonitor", String.format("0 0/%d * 1/1 * ? *", 5), startDate.getTime(), endDate.getTime());
    	SimpleTrigger loginMonitorTrigger = createSimpleTrigger("loginMonitor", 5, startDate.getTime(), endDate.getTime());
    	
		startDate = start(clockout, 0);
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
	
	private int[] getValue(String value){
		String [] values = StringUtils.split(value, ":");
		
		final int[] time = new int[values.length];
	    for (int i=0; i < values.length; i++) {
	        time[i] = Integer.parseInt(values[i]);
	    }
	    
	    return time;
	}
}
