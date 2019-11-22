package com.glogger.automation.page.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.glogger.automation.constants.Status;
import com.glogger.automation.exception.InvalidUsernamePasswordException;
import com.glogger.automation.exception.PasswordExpiredException;
import com.glogger.automation.page.ClockIn;
import com.glogger.automation.page.ClockOut;
import com.glogger.automation.page.Login;
import com.glogger.automation.page.Meal;
import com.glogger.automation.page.SignOut;
import com.glogger.automation.util.DateUtil;

public class ProcessTime{

	private static final Logger log = LogManager.getLogger(ProcessTime.class);

	public ProcessTime(Status action) {
		try {
			switch (action) {
			case LOGIN:
				log.info("Login Time: " + DateUtil.getInstance().getStartDate());
				Login login = new Login();
				login.login();
				break;
			case MEAL:
				new Meal();
				break;
			case CLOCK_IN:
				new ClockIn();
				break;
			case CLOCK_OUT:
				new ClockOut();
				break;
			case SIGN_OUT:
				new SignOut();
				break;
			}
		} catch (InvalidUsernamePasswordException e) {
			log.error(e);
		} catch (PasswordExpiredException e) {
			log.error(e);
		}
	}
}
