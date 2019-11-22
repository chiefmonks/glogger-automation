package com.glogger.automation.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public abstract class AbstractMonitorJob {

	public AbstractMonitorJob() {
		// TODO Auto-generated constructor stub
	}
	
	
	protected void monitor(){
		PropertyUtil util = PropertyUtil.getInstance();
		DateUtil dateUtil = DateUtil.getInstance();
		
		Object object_ = util.getDataFileProperty().getProperty(getPropertyKey());
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			execute();
		}else{
			Calendar date_ = GregorianCalendar.getInstance();
			date_.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			int duration = dateUtil.getMealDuration();
			if (!Constants.IS_DYNAMIC){
				duration = 20;
			}

			boolean notWithinRange = !dateUtil.isWithinInRange(date_.getTime(), 0, duration);
			if (notWithinRange){
				execute();
			}
		}
	}
	
	protected boolean isExecuted(){
		DateUtil util = DateUtil.getInstance();
		Date dateFromProperty = util.getDateFromProperty(getPropertyKey());
		
		if (dateFromProperty != null){
			return util.isInRange(util.getStartDate(), dateFromProperty, 7, 30);
		}
		
		return false;
	}
	
	protected boolean isLoggedIn(){
		return getDate(PropertyUtil.START) != null;
	}
	
	protected Date getDate(String key){
		return DateUtil.getInstance().getDateFromProperty(key);
	}
	
	protected abstract String getPropertyKey();
	protected abstract void execute();

}
