package com.glogger.automation.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glogger.automation.constants.Status;
import com.glogger.automation.page.action.ProcessTime;
import com.glogger.automation.util.PropertyUtil;

public class LoginJob extends AbstractJob {

	private static final Logger log = LogManager.getLogger(LoginJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		if (shouldExecute(PropertyUtil.START)){
			log.info("Login executed...");
			new ProcessTime(Status.LOGIN);
		}
		
	}

}
