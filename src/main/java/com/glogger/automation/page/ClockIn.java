package com.glogger.automation.page;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.constants.Status;
import com.glogger.automation.exception.InvalidUsernamePasswordException;
import com.glogger.automation.exception.PasswordExpiredException;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public class ClockIn extends AbstractPage{
	private static final Logger log = LogManager.getLogger(ClockIn.class);

	public ClockIn(){
		super();
		
		if (!isSessionValid()){
			log.info("Session Invalid, Re-Authenticating....");
			
			broadcast(StatusBar.class, null, "Session Invalid, Re-Authenticating....");
			
			Login login = new Login();
			try {
				login.login();
			} catch (InvalidUsernamePasswordException e) {
			} catch (PasswordExpiredException e) {
			}
		}
		
		changeToClockIn();
	}

	private void changeToClockIn(){
		com.glogger.automation.json.domain.Status status = getCurrentStatus();
		if (status.getStatusId() != Status.CLOCK_IN.ordinal() + 1){
			
			if (Constants.IS_DYNAMIC 
					&& isValidCurrentStatus(PropertyUtil.IN)){
				
				log.info("Status " + Status.CLOCK_IN + " is already executed..");
				broadcast(Glogger.class, MessageType.CLOCKIN, new Glogger(getDate(PropertyUtil.IN), Status.CLOCK_IN));
				
				switch (status.getStatusId()) {
				//clock-in
				case 1:
					broadcast(Glogger.class, MessageType.CLOCKIN, new Glogger(getDate(PropertyUtil.IN), Status.CLOCK_IN));
					break;
				//meal	
				case 2:
					broadcast(Glogger.class, MessageType.MEAL, new Glogger(getDate(PropertyUtil.MEAL), Status.MEAL));
					break;

				default:
					break;
				}
				
				return;
			}
			
			broadcast(StatusBar.class, null, "Executing clock-in..");
			
			changeStatus(Status.CLOCK_IN);
			
			Glogger glogger = new Glogger(new Date(), Status.CLOCK_IN);
			broadcast(Glogger.class, MessageType.CLOCKIN, glogger);
			
			saveDataProperty();
		}else{
			log.info("Current Status is already " + Status.CLOCK_IN);
			broadcast(Glogger.class, MessageType.CLOCKIN, new Glogger(getDate(PropertyUtil.IN), Status.CLOCK_IN));
		}
	}
	
	private Date getDate(String key){
		Date date = DateUtil.getInstance().getDateFromProperty(key);
		if (date == null)
			date = new Date();
		return date;
	}
	
	public void saveDataProperty(){
		PropertyUtil util = PropertyUtil.getInstance();
		DateUtil dateUtil = DateUtil.getInstance();
		
		String propertyKey = PropertyUtil.IN;
		
		Date date = new Date();
		if (Constants.IS_DYNAMIC){
			date = dateUtil.getMealOutDate();
		}
		
		Object object_ = util.getDataFileProperty().getProperty(propertyKey);
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			util.saveDataFileProperty(propertyKey, date.getTime());
		}else{
			Calendar clockInDate = GregorianCalendar.getInstance();
			clockInDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			boolean notWithinRange = !dateUtil.isWithinInRange(clockInDate.getTime(), 0, dateUtil.getMealDuration() - 10);
			if (notWithinRange){
				Calendar date_ = GregorianCalendar.getInstance();
				date_.setTime(date);
				
				util.saveDataFileProperty(propertyKey, date_.getTime().getTime());
			}
		}
	}
}
