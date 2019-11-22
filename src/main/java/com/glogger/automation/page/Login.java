package com.glogger.automation.page;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogger.automation.connection.ConnectionManager;
import com.glogger.automation.constants.Constants;
import com.glogger.automation.constants.Status;
import com.glogger.automation.exception.InvalidUsernamePasswordException;
import com.glogger.automation.exception.PasswordExpiredException;
import com.glogger.automation.json.domain.Credential;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.DataUtil;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.PropertyUtil;

public class Login extends AbstractPage {
	private static final Logger log = LogManager.getLogger(Login.class);

	public Login() {
		super();
	}

	protected Credential authenticate() {
		log.info("Authenticating crendentials... ");
		if (isSessionValid())
			return new Credential(true, String.valueOf(DataUtil.getInstance()
					.getEmployeeId()));

		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.KEY_USERNAME, Constants.USERNAME);
		data.put(Constants.KEY_PASSWORD, Constants.PASSWORD);

		Connection connection = Jsoup.connect(Constants.VERIFY_CREDENTIALS)
				.timeout(Integer.parseInt(Constants.TIME_OUT) * 1000)
				.method(Method.POST);
		connection.data(data);
		connection.header("Referer", Constants.URL);

		Response response = null;
		try {
			response = connection.execute();
		} catch (IOException e1) {

			ConnectionManager mgr = new ConnectionManager(
					Constants.VERIFY_CREDENTIALS, Method.POST);
			mgr.addHeader("Referer", Constants.URL);
			mgr.setData(data);

			response = mgr.execute();
		}

		if (response == null || response.statusCode() != 200) {
			return authenticate();
		}

		ObjectMapper objectMapper = new ObjectMapper();

		// convert json string to object
		try {
			Credential credentialReponse = objectMapper.readValue(
					response.body(), Credential.class);

			if (credentialReponse.getIsAuthenticated()) {
				DataUtil.getInstance().setCookies(response.cookies());
				DataUtil.getInstance().setEmployeeId(
						Integer.parseInt(credentialReponse.getEmployeeId()));

				broadcast(StatusBar.class, null, "Authenticated");
			}

			return credentialReponse;
		} catch (IOException e) {
			log.error("Error parsing credentials json.. " + response.body(), e);
			return new Credential(false, "");
		}
	}

	private void findLocation() {
		log.info("Setting office location.... ");

		if (Constants.OFFICE_LOCATION != null) {
			Map<String, String> data = new HashMap<String, String>();
			data.put(Constants.KEY_WINDOWS_LOGIN, Constants.USERNAME);
			data.put(Constants.KEY_EMP_ID,
					Integer.toString(DataUtil.getInstance().getEmployeeId()));

			ConnectionManager mgr = new ConnectionManager(
					Constants.GET_LOCATION, Method.POST);
			mgr.setData(data);

			Response response = mgr.execute();
			if (response == null) {
				findLocation();
			} else {
				try {
					Document document = response.parse();
					String locationId = null;

					Elements elements = document.select("option");
					for (Element element : elements) {
						if (element.text().trim()
								.equals(Constants.OFFICE_LOCATION.trim())) {
							locationId = element.val();
						}
					}

					DataUtil.getInstance().setLocationId(
							Integer.parseInt(locationId));
				} catch (IOException e) {
					log.info(
							"Something went wrong when getting office location.... ",
							e);
				}
			}
		}
	}

	public void login() throws InvalidUsernamePasswordException,
			PasswordExpiredException {
		Credential credentialReponse = authenticate();
		if (credentialReponse.getIsAuthenticated()) {

			// findLocation();

			processLogin(getCurrentStatus());

		} else {
			if (credentialReponse.getMessage().trim().toLowerCase()
					.contains("incorrect")) {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			} else {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			}
		}
	}

	private void processLogin(com.glogger.automation.json.domain.Status status) {
		log.info("Attempt to login.... ");
		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.KEY_USERNAME, Constants.USERNAME);
		data.put(Constants.KEY_PASSWORD, Constants.PASSWORD);
		data.put(Constants.KEY_INSTANCE_ID, Integer.toString(DataUtil.getInstance().getLocationId()));
		data.put(Constants.KEY_SUB_GROUP, Integer.toString(DataUtil.getInstance().getSubGroupId()));
		data.put(Constants.KEY_REAL_DIFFERENCE, Integer.toString(Constants.REAL_DIFFERENCE));
		data.put(Constants.KEY_LOGIN_EMP_ID, Integer.toString(DataUtil.getInstance().getEmployeeId()));

		String statusId = String.valueOf(Status.CLOCK_IN.ordinal() + 1);
		if (status.getStatusId() != null) {
			statusId = String.valueOf(status.getStatusId());
		} else {
			PropertyUtil.getInstance().saveDataFileProperty(PropertyUtil.OUT, "");
			PropertyUtil.getInstance().saveDataFileProperty(PropertyUtil.LOG, "");

			broadcast(Login.class, null, "New Shift..");

		}
		data.put(Constants.KEY_STATUS_ID, statusId);

		// only execute once on login
		if (DataUtil.getInstance().getUserIp() == null) {
			ConnectionManager mgr = new ConnectionManager(Constants.LOGIN, Method.POST);
			mgr.setData(data);

			Response response = mgr.execute();
			if (response == null) {
				processLogin(status);
			} else {
				userHome();

				getGloggerLogTime();

				if (status.getStatusId() == null) {
					saveDataProperty();
				}

				log.info("Login successfull...");

				broadcast(StatusBar.class, null, "Login successfull...");
			}

			status = getCurrentStatus();

			broadcast(Glogger.class, MessageType.LOGIN, new Glogger(getDate(PropertyUtil.LOG), Status.LOGIN));
		}

		switch (status.getStatusId()) {
		// CLOCK IN
		case 1:
			if (hasMeal()) {
				broadcast(Glogger.class, MessageType.CLOCKIN, new Glogger(getDate(PropertyUtil.IN), Status.CLOCK_IN));
			}

			break;
		// MEAL
		case 2:
			broadcast(Glogger.class, MessageType.MEAL, new Glogger(getDate(PropertyUtil.MEAL), Status.MEAL));
			break;
		// LOGIN

		default:
			// do nothing
			break;
		}
	}

	public void loginNoShift() throws InvalidUsernamePasswordException,
			PasswordExpiredException {
		Credential credentialReponse = authenticate();
		if (credentialReponse.getIsAuthenticated()) {

			processLoginNoShift();

		} else {
			if (credentialReponse.getMessage().trim().toLowerCase()
					.contains("incorrect")) {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			} else {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			}
		}
	}

	private void processLoginNoShift() {
		log.info("Attempt to login with no shift ");

		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.KEY_USERNAME, Constants.USERNAME);
		data.put(Constants.KEY_PASSWORD, Constants.PASSWORD);
		data.put(Constants.KEY_INSTANCE_ID,
				Integer.toString(DataUtil.getInstance().getLocationId()));
		data.put(Constants.KEY_SUB_GROUP, "0");
		data.put(Constants.KEY_REAL_DIFFERENCE,
				Integer.toString(Constants.REAL_DIFFERENCE));
		data.put(Constants.KEY_LOGIN_EMP_ID,
				Integer.toString(DataUtil.getInstance().getEmployeeId()));

		ConnectionManager mgr = new ConnectionManager(Constants.LOGIN,
				Method.POST);
		mgr.setData(data);

		Response response = mgr.execute();
		if (response == null) {
			processLoginNoShift();
		}

		broadcast(StatusBar.class, null, "Login with no shift successfull...");
	}

	public boolean loginForActivities()
			throws InvalidUsernamePasswordException, PasswordExpiredException {
		boolean isLoginWithShift = true;

		Credential credentialReponse = authenticate();
		if (credentialReponse.getIsAuthenticated()) {

			com.glogger.automation.json.domain.Status status = getCurrentStatus();
			if (status.getStatusId() != null) {
				processLogin(status);
				isLoginWithShift = true;
			} else {
				processLoginNoShift();
				isLoginWithShift = false;
			}
		} else {
			if (credentialReponse.getMessage().trim().toLowerCase()
					.contains("incorrect")) {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			} else {
				broadcast(Credential.class, null, credentialReponse);
				throw new InvalidUsernamePasswordException(credentialReponse,
						credentialReponse.getMessage());
			}
		}

		return isLoginWithShift;
	}

	private Date getDate(String key) {
		Date date = DateUtil.getInstance().getDateFromProperty(key);
		if (date == null)
			date = new Date();
		return date;
	}

	public static void main(String[] args) {
		Login login = new Login();
		try {
			login.login();
			new ClockIn();
		} catch (InvalidUsernamePasswordException e) {
			System.out.println(e.getCredential());
			e.printStackTrace();
		} catch (PasswordExpiredException e) {
			System.out.println(e.getCredential());
			e.printStackTrace();
		}

		// File input = new File("input.html");
		// try {
		// Document doc = Jsoup.parse(input, "UTF-8");
		// System.out.println(login.getRealDiff(doc));
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// Document doc = Jsoup.parse("");

		// Credential credentialReponse = login.authenticate();
		// System.out.println(credentialReponse.getIsAuthenticated());
		// System.out.println(credentialReponse.getMessage());
		// if (credentialReponse.getIsAuthenticated()){
		//
		// login.findLocation();
		// System.out.println(login.getCurrentStatus());
		// }else{
		// //alert error
		// }

	}

	private void saveDataProperty() {
		PropertyUtil util = PropertyUtil.getInstance();
		DateUtil dateUtil = DateUtil.getInstance();

		Object object_ = util.getDataFileProperty().getProperty(PropertyUtil.START);
		
		Date date = new Date();
		if (Constants.IS_DYNAMIC){
			date = dateUtil.getStartDate();
		}
		
		if (object_ == null || String.valueOf(object_).trim().isEmpty()) {
			util.saveDataFileProperty(PropertyUtil.START, date.getTime());
		} else {
			Calendar loginDate = GregorianCalendar.getInstance();
			loginDate.setTimeInMillis(Long.valueOf(String.valueOf(object_)));

			boolean notWithinRange = !dateUtil.isWithinInRange(loginDate.getTime(), 8, dateUtil.getMealDuration() + 5);
			if (notWithinRange) {
				util.saveDataFileProperty(PropertyUtil.START, date.getTime());
			}
		}
	}
	
	private boolean hasMeal(){
		DateUtil util = DateUtil.getInstance();
		Date meal = util.getDateFromProperty(PropertyUtil.MEAL);
		Date start = util.getDateFromProperty(PropertyUtil.START);
		
		if (meal == null || start == null)
			return false;
		
		return util.isInRange(start, meal, 7, 30);
	}

	private void getGloggerLogTime() {
		PropertyUtil util = PropertyUtil.getInstance();
		DateUtil dateUtil = DateUtil.getInstance();

		Object object_ = util.getDataFileProperty().getProperty(PropertyUtil.LOG);
		if (String.valueOf(object_).trim().isEmpty()) {

			Calendar calendar = DataUtil.getInstance().getGloggerTime();

			boolean isWithin5Minutes = dateUtil.isWithInRange(dateUtil.getStartDate(), calendar.getTime(), 5);
			if (!isWithin5Minutes) {
				util.saveDataFileProperty(PropertyUtil.START, calendar.getTimeInMillis());
				util.saveDataFileProperty(PropertyUtil.LOG, calendar.getTimeInMillis());
			}

			broadcast(Glogger.class, MessageType.LOGIN, new Glogger(calendar.getTime(), Status.LOGIN));
		}
	}
}
