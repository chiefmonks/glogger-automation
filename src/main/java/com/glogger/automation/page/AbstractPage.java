package com.glogger.automation.page;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogger.automation.connection.ConnectionManager;
import com.glogger.automation.constants.Constants;
import com.glogger.automation.json.domain.Status;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.json.domain.StatusWrapper;
import com.glogger.automation.service.MessageEnabledService;
import com.glogger.automation.util.DataUtil;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public abstract class AbstractPage extends MessageEnabledService implements IPage {

	private static final Logger log = LogManager.getLogger(AbstractPage.class);
	
	public AbstractPage(){
		super();
	}
	
	protected boolean isSessionValid() {
		Response response =  new ConnectionManager(Constants.VERIFY_SESSION, Method.GET).execute();
		
		if (response != null && response.statusCode() == 200){
			return new Boolean(response.body());
		}

		return isSessionValid();
	}

	public void changeStatus(com.glogger.automation.constants.Status status) {
		log.info("Change status to " + status.name());
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.KEY_SUB_GROUP, String.valueOf(DataUtil.getInstance().getSubGroupId()));
		data.put(Constants.KEY_STATUS_ID, String.valueOf(status.ordinal() + 1));
		data.put(Constants.KEY_USER_IP, DataUtil.getInstance().getUserIp());
		
		ConnectionManager mgr = new ConnectionManager(Constants.CHANGE_STATUS, Method.POST);
		mgr.addHeader("Referer", Constants.HOME);
		mgr.setData(data);
		
		Response response = mgr.execute();
		if (response == null){
			changeStatus(status);
		}
//		else{
//			if (response.statusCode() != 200 && response.statusCode() != 302
//					&& response.statusCode() != 301) {
//				changeStatus(status);
//			}
//		}
		
		broadcast(StatusBar.class, null, "Status changed to " + status.name());
	}
	
	public Status getCurrentStatus(){
		log.info("Getting current status... ");
		
		broadcast(StatusBar.class, null, "Getting current status... ");
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.KEY_WINDOWS_LOGIN, Constants.USERNAME);
		data.put(Constants.KEY_EMP_ID, Integer.toString(DataUtil.getInstance().getEmployeeId()));
		data.put(Constants.KEY_INSTANCE_ID, Integer.toString(DataUtil.getInstance().getLocationId()));
		
		ConnectionManager mgr = new ConnectionManager(Constants.SUB_GROUP, Method.POST);
		mgr.setData(data);
		
		Response response = mgr.execute();
		if (response == null || response.statusCode() != 200){
			return getCurrentStatus();
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		//convert json string to object
		try {
			Status status = null;

			if (response.body().contains(Constants.JSON_SUB_GROUP_LIST)){

				StatusWrapper wrapper =  objectMapper.readValue(response.body(), StatusWrapper.class);
				for (Status status_ : wrapper.getStatus()){
					if (status_.getSubGroupName().trim().equals(Constants.SUB_GROUP_NAME.trim())){
						status = status_;
					}
				}
				
			}else{
				status =  objectMapper.readValue(response.body(), Status.class);
			}
			
			DataUtil.getInstance().setSubGroupId(status.getSubGroupId());

			return status;
		} catch (IOException e) {
			log.error("Error parsing status json.. " + response.body(), e );
		}
		return null;
	}
	
	protected void userHome(){
		log.info("Accessing home page..");
		Response response =  new ConnectionManager(Constants.HOME, Method.GET).execute();

		try {
			Document document = response.parse();
			
			if (DataUtil.getInstance().getGloggerTime() == null){
				Element hour = document.select("#hhAgent2").first();
				Element minute = document.select("#mmAgent2").first();
				Element second = document.select("#ssAgent2").first();
				
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.add(Calendar.HOUR, -Integer.valueOf(hour.text()));
				calendar.add(Calendar.MINUTE, -Integer.valueOf(minute.text()));
				calendar.add(Calendar.SECOND, -Integer.valueOf(second.text()));
				DataUtil.getInstance().setGloggerTime(calendar);
			}
			
			DataUtil.getInstance().setUserIp(getUserIp(document));
		} catch (IOException e) {
			log.info("Something went wrong when getting to home page.... ", e);
		}
	}
	
	//this can be get from user home page
	protected String getUserIp(Document doc){
		String ip = null;
		Elements script = doc.select("script"); // Get the script part
		
		for (Element element : script){
			
			Pattern p = Pattern.compile("(?is)ipToSend = \"(.+?)\""); // Regex for the value of the key
			Matcher m = p.matcher(element.html()); // you have to use html here and NOT text! Text will drop the 'key' part
			
			while( m.find() )		{
				ip = m.group(1);
			}
			
			if (ip != null)
				break;
		}
		
		return ip;
	}
	
	protected boolean isValidCurrentStatus(String key){
		DateUtil util = DateUtil.getInstance();
		Date dateFromProperty = util.getDateFromProperty(key);
		
		if (dateFromProperty != null){
			
			Date startDate = util.getDateFromProperty(PropertyUtil.LOG);
			if (startDate == null)
				startDate = util.getStartDate();
			
			return util.isInRange(util.getStartDate(), dateFromProperty, 7, 30);
		}
		
		return false;
	}
}
