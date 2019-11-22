package com.glogger.automation.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import com.glogger.automation.constants.Constants;

public class DateUtil {
	
	public static DateUtil instance;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
	
	private DateUtil() {}
	
	public static DateUtil getInstance(){
		if (instance == null)
			instance = new DateUtil();
		return instance;
	}
	
	public Date getStartDate(){
		PropertyUtil util = PropertyUtil.getInstance();
		Object object_ = util.getDataFileProperty().getProperty(PropertyUtil.START);
		
		if (object_ == null || String.valueOf(object_).trim().isEmpty()){
			return Constants.START_TIME.getTime();
		}else{
			Calendar propertyDate = GregorianCalendar.getInstance();
			propertyDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			if (this.isWithinInRange(propertyDate.getTime(), 8, this.getMealDuration() + 5)){
				return propertyDate.getTime();
			}
			
			return Constants.START_TIME.getTime();
		}
	}
	
	public Date getEndDate(){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(this.getStartDate());
		calendar.add(Calendar.HOUR, 8);
		calendar.add(Calendar.MINUTE, this.getMealDuration() + this.getTimeBuffer());
		
		return calendar.getTime();
	}
	
	public Date getMealDate(){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(this.getStartDate());
		calendar.add(Calendar.HOUR, Integer.valueOf(Constants.MEAL_TIME));
		
		return calendar.getTime();
	}
	
	public Date getMealOutDate(){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(this.getMealDate());
		calendar.add(Calendar.MINUTE, this.getMealDuration());
		
		return calendar.getTime();
	}
		
	public int getMealDuration(){
		if (Constants.MEAL_DURATION == null || Constants.MEAL_DURATION.trim().isEmpty())
			return 60;
		return Integer.valueOf(Constants.MEAL_DURATION);
	}
	
	public int getTimeBuffer(){
		if (Constants.TIME_BUFFER == null || Constants.TIME_BUFFER.trim().isEmpty())
			return 5;
		return Integer.valueOf(Constants.TIME_BUFFER);
	}
	
	public Date getCurrentDate(){
		return GregorianCalendar.getInstance().getTime();
	}
	
	private boolean isWithInRange(Date baseDate, Integer hour, Integer minute){
		Calendar currentDate = Calendar.getInstance();

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(baseDate);
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTime(startDate.getTime());
		
		if (hour != null){
			expectedDate.add(Calendar.HOUR, hour);
		}
		if (minute != null){
			expectedDate.add(Calendar.MINUTE, minute);
		}
		
		return (currentDate.after(startDate) && currentDate.before(expectedDate));
		
	}
	
	private boolean isWithInRange(Date start, Date end, Integer hour, Integer minute){
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(start);

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(end);
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTime(startDate.getTime());
		
		if (hour != null){
			expectedDate.add(Calendar.HOUR, hour);
		}
		if (minute != null){
			expectedDate.add(Calendar.MINUTE, minute);
		}
		
		return (endDate.after(startDate) && endDate.before(expectedDate));
		
	}
	
	public boolean isWithInRange(Date date1, Date date2, int minute){
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date1);

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date2);
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTime(startDate.getTime());
		expectedDate.add(Calendar.MINUTE, minute);
		
		return (currentDate.after(startDate) && currentDate.before(expectedDate));
		
	}
	
	public boolean isWithinInRange(Date baseDate, int hours, int minutes){
		Integer hour = hours == 0 ? null : new Integer(hours);
		Integer minute = minutes == 0 ? null : new Integer(minutes);
		
		return isWithInRange(baseDate, hour, minute);
	}
	
	public boolean isInRange(Date start, Date end, int hours, int minutes){
		Integer hour = hours == 0 ? null : new Integer(hours);
		Integer minute = minutes == 0 ? null : new Integer(minutes);
		
		return isWithInRange(start, end, hour, minute);
	}
	
	public boolean isInRange(Date start, Date end, int hours){
		return isInRange(start, end, hours, 0);
	}
	
	public boolean isWithinInRange(Date baseDate, int hours){
		return isWithinInRange(baseDate, hours, 0);
	}
	
	public String getDate(Date date){
		return dateFormat.format(date);
	}
	
	public String getDate(Date date, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public Date getDateFromProperty(String key){
		PropertyUtil util = PropertyUtil.getInstance();
		
		Object object_ = util.getDataFileProperty().getProperty(key);
		if (object_ != null && !String.valueOf(object_).trim().isEmpty()){
			Calendar propertyDate = GregorianCalendar.getInstance();
			propertyDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));
			
			return propertyDate.getTime();
		}
		
		return null;
	}
	
	public Date getDate(String value){
		String[] values = StringUtils.split(value, ":");
		if (values.length > 1){
			return getDate(Integer.valueOf(values[0]), Integer.valueOf(values[1]));
		}else{
			return getDate(Integer.valueOf(values[0]));
		}
	}
	
	public Date getDate(int hour){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		return calendar.getTime();
	}
	
	public Date getDate(int hour, int minute){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		
		return calendar.getTime();
	}
	
	public Date getDateAddHour(Date date, int hour){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		Calendar start = GregorianCalendar.getInstance();
		System.out.println(start.getTime());
		
		System.out.println(DateUtil.getInstance().getDateAddHour(start.getTime(), 1));
		
//		Calendar end = GregorianCalendar.getInstance();
//		end.add(Calendar.HOUR, 2);
//		
//		System.out.println("start " + start.getTime().toString());
//		System.out.println("end " + end.getTime().toString());
//		
//		boolean value = DateUtil.getInstance().isInRange(start.getTime(), end.getTime(), 2, 0);
//		System.out.println(value);
	}
}
