package com.glogger.automation.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glogger.automation.constants.Status;
import com.glogger.automation.page.action.ProcessTime;
import com.glogger.automation.util.PropertyUtil;

public class LoginMonitorJob extends AbstractMonitorJob implements Job {
	
	private static final Logger log = LogManager.getLogger(LoginMonitorJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		log.info("Monitor Login...");
		
		monitor();
		
	}

	@Override
	protected String getPropertyKey() {
		return PropertyUtil.START;
	}

	@Override
	protected void execute() {
		
		boolean execute = false;
		
		if (getDate(PropertyUtil.START) == null && getDate(PropertyUtil.OUT) != null){
			execute = true;
		} else if (getDate(PropertyUtil.START) == null && getDate(PropertyUtil.OUT) == null){
			execute = true;
		} else if (getDate(PropertyUtil.START) == null){
			execute = true;
		}
		
		if (execute){
			log.info("Login re-executed...");
			new ProcessTime(Status.LOGIN);
		}
	}
}
