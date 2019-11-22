package com.glogger.automation.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glogger.automation.constants.Status;
import com.glogger.automation.page.action.ProcessTime;
import com.glogger.automation.util.PropertyUtil;

public class MealMonitorJob extends AbstractMonitorJob implements Job {
	
	private static final Logger log = LogManager.getLogger(MealMonitorJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.info("Monitoring meal...");
		
		monitor();
		
	}

	@Override
	protected String getPropertyKey() {
		return PropertyUtil.MEAL;
	}

	@Override
	protected void execute() {
		if (isLoggedIn() && !isExecuted()){
			log.info("Meal re-executed...");
			new ProcessTime(Status.MEAL);
		}
	}
}
