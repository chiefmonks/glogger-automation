package com.glogger.automation.job;

import java.util.Date;

import org.quartz.Job;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public abstract class AbstractJob implements Job{

	protected boolean shouldExecute(String key){
		if (!Constants.IS_DYNAMIC)
			return true;
		return Constants.IS_DYNAMIC && isValidCurrentStatus(key);
	}
	
	private boolean isValidCurrentStatus(String key){
		DateUtil util = DateUtil.getInstance();
		Date dateFromProperty = util.getDateFromProperty(key);
		
		if (dateFromProperty != null){
			
			Date startDate = util.getDateFromProperty(PropertyUtil.LOG);
			if (startDate == null)
				startDate = util.getStartDate();
			
			return util.isInRange(startDate, dateFromProperty, 7, 30);
		}
		
		return false;
	}
}
