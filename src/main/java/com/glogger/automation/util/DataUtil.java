package com.glogger.automation.util;

import java.util.Calendar;
import java.util.Map;

import com.glogger.automation.constants.Constants;

public class DataUtil {

	private static DataUtil instance = new DataUtil();

	private Map<String, String> cookies;

	private String userIp;
	
	private int employeeId;
	
	private int locationId = Constants.LOCATION_ID;
	
	private int subGroupId;
	
	private Calendar gloggerTime;
	
	private DataUtil() {

	}

	public static DataUtil getInstance() {
		return instance;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(int subGroupId) {
		this.subGroupId = subGroupId;
	}

	public Calendar getGloggerTime() {
		return gloggerTime;
	}

	public void setGloggerTime(Calendar gloggerTime) {
		this.gloggerTime = gloggerTime;
	}
}
