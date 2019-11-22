package com.glogger.automation.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glogger.automation.constants.Status;
import com.glogger.automation.page.action.ProcessTime;

public class ClockOutJob implements Job {
	
	private static final Logger log = LogManager.getLogger(ClockOutJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		log.info("Clock out executed...");
		new ProcessTime(Status.CLOCK_OUT);
		
		log.info("Signing out executed...");
		new ProcessTime(Status.SIGN_OUT);
	}

}
