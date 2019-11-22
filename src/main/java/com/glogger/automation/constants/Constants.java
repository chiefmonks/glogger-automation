package com.glogger.automation.constants;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.glogger.automation.util.PropertyUtil;

public interface Constants {
	
	public static final Calendar START_TIME = GregorianCalendar.getInstance();
	
	static final String URL = PropertyUtil.getInstance().getProperty("url");
	
	static final String USERNAME = PropertyUtil.getInstance().getProperty("username");
	
	static final String PASSWORD = PropertyUtil.getInstance().getProperty("password");
	
	static final String MEAL_TIME = PropertyUtil.getInstance().getProperty("meal-time");
	
	static final String MEAL_DURATION = PropertyUtil.getInstance().getProperty("meal-duration");
	
	static final String TIME_BUFFER = PropertyUtil.getInstance().getProperty("time-buffer");
	
	static final String TIME_OUT = PropertyUtil.getInstance().getProperty("time-out");
	
	static final String SUB_GROUP_NAME = PropertyUtil.getInstance().getProperty("sub-group");
	
	static final String OFFICE_LOCATION = PropertyUtil.getInstance().getProperty("office-location");
	
	static final String REMOTE_ADDRESS = PropertyUtil.getInstance().getProperty("remoteAddress");
	
	static final String PROXY_HOST = PropertyUtil.getInstance().getProperty("http.proxyHost");
	
	static final String PROXY_PORT = PropertyUtil.getInstance().getProperty("http.proxyPort");
	
	static final String FIX_LOGIN = PropertyUtil.getInstance().getProperty("fix-login");
	
	static final String FIX_MEAL = PropertyUtil.getInstance().getProperty("fix-meal");
	
	static final String FIX_CLOCKIN = PropertyUtil.getInstance().getProperty("fix-clockin");
	
	static final String FIX_LOGOUT = PropertyUtil.getInstance().getProperty("fix-logout");
	
	static final Boolean IS_DYNAMIC = new Boolean(PropertyUtil.getInstance().getProperty("dynamic-schedule"));
	
	static final int RETRIES = 3;
	
	static final int SUB_GROUP_ID = 222;
	
	static final int REAL_DIFFERENCE = 4;
	
	static final int LOCATION_ID = 4;
	
	
	static final String HOME = URL + "user_home";
	
	static final String LOGIN = URL + "loggin/loggin";
	
	static final String VERIFY_SESSION = URL + "loggin/VerifySession";
	
	static final String CHANGE_STATUS = URL + "user_home/changeStatus";
	
	static final String END_SHIFT = URL + "user_home/endShift";

	static final String VERIFY_CREDENTIALS = URL + "loggin/VerifyCredentials";
	
	static final String GET_LOCATION = URL + "loggin/getInstance";
	
	static final String SUB_GROUP = URL + "loggin/getSubgroup";
	
	static final String GET_LAST_SHIFT = URL + "loggin/GetLastShift";
	
	static final String SIGN_OUT = URL + "loggin/Logout";
	
	static final String RECENT_ACTIVITIES = URL + "user_home/GetRecentActivities";

	
	
	static final String KEY_USERNAME = "username";
	
	static final String KEY_PASSWORD = "password";
	
	static final String KEY_WINDOWS_LOGIN = "windows_login";
	
	static final String KEY_SUB_GROUP = "subgroup_id";
	
	static final String KEY_STATUS_ID = "status_id";
	
	static final String KEY_USER_IP = "user_ip";
	
	static final String KEY_INSTANCE_ID = "instance_id";
	
	static final String KEY_EMP_ID = "emp_id";
	
	static final String KEY_REAL_DIFFERENCE = "realDiference";
	
	static final String KEY_LOGIN_EMP_ID = "EmpId";
	
	static final String JSON_SUB_GROUP_LIST = "subgroups";
	
}
