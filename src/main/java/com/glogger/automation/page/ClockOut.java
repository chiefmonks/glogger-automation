package com.glogger.automation.page;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.glogger.automation.connection.ConnectionManager;
import com.glogger.automation.constants.Constants;
import com.glogger.automation.constants.Status;
import com.glogger.automation.exception.InvalidUsernamePasswordException;
import com.glogger.automation.exception.PasswordExpiredException;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public class ClockOut  extends AbstractPage{
	private static final Logger log = LogManager.getLogger(ClockOut.class);

	public ClockOut(){
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
		
		clockOut();
	}
	
	private void clockOut(){
		log.info("Ending shift... ");
		
		broadcast(StatusBar.class, null, "Executing clock-out..");
		
		ConnectionManager mgr = new ConnectionManager(Constants.END_SHIFT, Method.POST);
		mgr.addHeader("Referer", Constants.HOME);
		
		Response response =  mgr.execute();

		if (response == null){
			clockOut();
		}
		
		Glogger glogger = new Glogger(new Date(), Status.CLOCK_OUT);
		broadcast(Glogger.class, MessageType.LOGOUT, glogger);
		
		saveDataProperty();
	}

	
	public void saveDataProperty(){
		PropertyUtil util = PropertyUtil.getInstance();
		DateUtil dateUtil = DateUtil.getInstance();
		
		String propertyKey = PropertyUtil.END;
		
		Object object_ = util.getDataFileProperty().getProperty(propertyKey);
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			util.saveDataFileProperty(propertyKey, dateUtil.getCurrentDate().getTime());
		}else{
			Calendar clockOutDate = GregorianCalendar.getInstance();
			clockOutDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			boolean notWithinRange = !dateUtil.isWithinInRange(clockOutDate.getTime(), 0, dateUtil.getMealDuration() - 10);
			if (notWithinRange){
				Calendar date_ = GregorianCalendar.getInstance();
				
				util.saveDataFileProperty(propertyKey, date_.getTime().getTime());
			}
		}
	}
}
