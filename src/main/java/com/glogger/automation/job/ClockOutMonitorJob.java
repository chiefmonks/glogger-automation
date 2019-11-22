package com.glogger.automation.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glogger.automation.constants.Status;
import com.glogger.automation.page.action.ProcessTime;
import com.glogger.automation.util.PropertyUtil;

public class ClockOutMonitorJob extends AbstractMonitorJob implements Job {
	
	private static final Logger log = LogManager.getLogger(ClockOutMonitorJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		log.info("Monitoring clock out...");
		
		Object object_ = PropertyUtil.getInstance().getDataFileProperty().getProperty(PropertyUtil.OUT);
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			monitor();
		}

	}
	
	@Override
	protected String getPropertyKey() {
		return PropertyUtil.END;
	}

	@Override
	protected void execute() {
		log.info("Clock out re-executed...");
		new ProcessTime(Status.CLOCK_OUT);
		
		log.info("Signing out re-executed...");
		new ProcessTime(Status.SIGN_OUT);
	}

}
