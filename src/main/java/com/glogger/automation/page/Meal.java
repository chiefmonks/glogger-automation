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

public class Meal extends AbstractPage{
	private static final Logger log = LogManager.getLogger(Meal.class);

	public Meal(){
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
		
		changeToMeal();
	}
	
	private void changeToMeal(){
		com.glogger.automation.json.domain.Status status = getCurrentStatus();
		if (status.getStatusId() != Status.MEAL.ordinal() + 1){
			
			if (Constants.IS_DYNAMIC 
					&& isValidCurrentStatus(PropertyUtil.MEAL)){
				
				log.info("Status " + Status.MEAL + " is already executed..");
				
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
			
			broadcast(StatusBar.class, null, "Executing meal..");

			changeStatus(Status.MEAL);
			
			Glogger glogger = new Glogger(new Date(), Status.MEAL);
			broadcast(Glogger.class, MessageType.MEAL, glogger);
			
			saveDataProperty();
		}else{
			log.info("Current Status is already " + Status.MEAL);
			broadcast(Glogger.class, MessageType.MEAL, new Glogger(getDate(PropertyUtil.MEAL), Status.MEAL));
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
		
		String propertyKey = PropertyUtil.MEAL;
		
		Date date = new Date();
		if (Constants.IS_DYNAMIC){
			date = dateUtil.getMealDate();
		}
		
		Object object_ = util.getDataFileProperty().getProperty(propertyKey);
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			util.saveDataFileProperty(propertyKey, date.getTime());
		}else{
			Calendar mealDate = GregorianCalendar.getInstance();
			mealDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			boolean notWithinRange = !dateUtil.isWithinInRange(mealDate.getTime(), 0, dateUtil.getMealDuration() - 10);
			if (notWithinRange){
				util.saveDataFileProperty(propertyKey, date.getTime());
			}
		}
	}
}
