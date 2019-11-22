package com.glogger.automation.page;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.glogger.automation.connection.ConnectionManager;
import com.glogger.automation.constants.Constants;
import com.glogger.automation.constants.Status;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.PropertyUtil;

public class SignOut extends AbstractPage{
	private static final Logger log = LogManager.getLogger(SignOut.class);

	public SignOut(){
		super();
		
		if (!isSessionValid()){
			log.info("Session Invalid, Re-Authenticating....");
			
			broadcast(StatusBar.class, null, "Session Invalid, Re-Authenticating....");
			
			Login login = new Login();
			login.authenticate();
//			try {
//				login.loginNoShift();
//			} catch (InvalidUsernamePasswordException e) {
//			} catch (PasswordExpiredException e) {
//			}
		}
		
		signOut();
		
	}
	
	private void signOut(){
		log.info("Signing out... ");
		
		broadcast(StatusBar.class, null, "Executing sign-out..");
		
		ConnectionManager mgr = new ConnectionManager(Constants.SIGN_OUT, Method.GET);
		mgr.addHeader("Referer", Constants.HOME);
		
		Response response =  mgr.execute();
		
		if (response == null){
			signOut();
		}
		
		Glogger glogger = new Glogger(new Date(), Status.SIGN_OUT);
		broadcast(Glogger.class, MessageType.SIGNOUT, glogger);
		
		saveDataProperty();
	}
	
	public void saveDataProperty(){
		PropertyUtil.getInstance().clearDataFileProperty();
		PropertyUtil.getInstance().saveDataFileProperty(PropertyUtil.OUT, GregorianCalendar.getInstance().getTimeInMillis());
	}
}
