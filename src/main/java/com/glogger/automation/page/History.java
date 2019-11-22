package com.glogger.automation.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogger.automation.connection.ConnectionManager;
import com.glogger.automation.constants.Constants;
import com.glogger.automation.exception.InvalidUsernamePasswordException;
import com.glogger.automation.exception.PasswordExpiredException;
import com.glogger.automation.json.domain.Activity;
import com.glogger.automation.json.domain.StatusBar;

public class History extends AbstractPage{
	private static final Logger log = LogManager.getLogger(History.class);

	public History(){
		super();
		
		boolean isLoginWithShift = true;
		boolean hasError = false;
		
		if (!isSessionValid()){
			log.info("Session Invalid, Re-Authenticating....");
			Login login = new Login();
			try {
				isLoginWithShift = login.loginForActivities();
			} catch (InvalidUsernamePasswordException e) {
				hasError = !hasError;
			} catch (PasswordExpiredException e) {
				hasError = !hasError;
			}
		}
		
		getRecentActivites();
		
		if (!hasError && !isLoginWithShift){
			signOut();
		}
	}
	
	
	private void getRecentActivites(){
		log.info("Get recent activites....");
		
		broadcast(StatusBar.class, null, "Get recent activites....");
		
		ConnectionManager mgr = new ConnectionManager(Constants.RECENT_ACTIVITIES, Method.POST);
		mgr.addHeader("Referer", Constants.HOME);
		
		Response response =  mgr.execute();
		
		if (response == null){
			getRecentActivites();
		}
		
//		String json = "[{\"log_id\":\"1464125\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-27 01:37:11\",\"log_end_date\":\"2016-07-27 05:42:10\",\"log_duration\":\"04:04:59\",\"shift_id\":\"394773\",\"log_ip\":\"206.108.31.34\"},{\"log_id\":\"1463889\",\"sta_status\":\"Meal\",\"log_start_date\":\"2016-07-27 00:57:11\",\"log_end_date\":\"2016-07-27 01:37:11\",\"log_duration\":\"00:40:00\",\"shift_id\":\"394773\",\"log_ip\":\"206.108.31.34\"},{\"log_id\":\"1462867\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-26 20:57:16\",\"log_end_date\":\"2016-07-27 00:57:11\",\"log_duration\":\"03:59:55\",\"shift_id\":\"394773\",\"log_ip\":\"206.108.31.34\"},{\"log_id\":\"1459080\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-26 02:03:17\",\"log_end_date\":\"2016-07-26 06:11:47\",\"log_duration\":\"04:08:30\",\"shift_id\":\"393413\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1458696\",\"sta_status\":\"Meal\",\"log_start_date\":\"2016-07-26 01:00:40\",\"log_end_date\":\"2016-07-26 02:03:17\",\"log_duration\":\"01:02:37\",\"shift_id\":\"393413\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1457707\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-25 20:51:14\",\"log_end_date\":\"2016-07-26 01:00:40\",\"log_duration\":\"04:09:26\",\"shift_id\":\"393413\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1447708\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-23 01:28:53\",\"log_end_date\":\"2016-07-23 05:33:52\",\"log_duration\":\"04:04:59\",\"shift_id\":\"391017\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1447580\",\"sta_status\":\"Meal\",\"log_start_date\":\"2016-07-23 00:59:10\",\"log_end_date\":\"2016-07-23 01:28:53\",\"log_duration\":\"00:29:43\",\"shift_id\":\"391017\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1446630\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-22 20:58:40\",\"log_end_date\":\"2016-07-23 00:59:10\",\"log_duration\":\"04:00:30\",\"shift_id\":\"391017\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1442847\",\"sta_status\":\"Clock In\",\"log_start_date\":\"2016-07-22 01:36:27\",\"log_end_date\":\"2016-07-22 05:41:27\",\"log_duration\":\"04:05:00\",\"shift_id\":\"389809\",\"log_ip\":\"206.108.31.36\"},{\"log_id\":\"1442705\",\"sta_status\":\"Meal\",\"log_start_date\":\"2016-07-22 01:06:27\",\"log_end_date\":\"2016-07-22 01:36:27\",\"log_duration\":\"00:30:00\",\"shift_id\":\"389809\",\"log_ip\":\"206.108.31.36\"}]";
		
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Activity.class) ;
		
		List<Activity> activities = new ArrayList<Activity>();
		
		//convert json string to object
		try {
			activities = objectMapper.readerFor(type).readValue(response.body());
			
			broadcast(Activity.class, null, activities);
			
			broadcast(StatusBar.class, null, "Activites loaded..");
			
		} catch (IOException e) {
			log.error("Error parsing activities json.. " + response.body(), e );
		}
	}
	
	private void signOut(){
		log.info("Signing out... ");
		
		broadcast(StatusBar.class, null, "Executing sign-out...");
		
		ConnectionManager mgr = new ConnectionManager(Constants.SIGN_OUT, Method.GET);
		mgr.addHeader("Referer", Constants.HOME);
		
		Response response =  mgr.execute();
		if (response == null){
			signOut();
		}
	}
	
	public static void main(String[] args) {
		History history = new History();
		history.getRecentActivites();
	}
}
