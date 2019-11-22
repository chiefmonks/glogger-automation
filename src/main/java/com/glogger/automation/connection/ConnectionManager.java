package com.glogger.automation.connection;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.util.DataUtil;

public class ConnectionManager extends AbstractConnection {

	private static final Logger log = LogManager
			.getLogger(ConnectionManager.class);

	private Map<String, String> headers = new HashMap<String, String>();
	private String url;
	private Connection.Method method;
	private Map<String, String> data;
	private Boolean followRedirect;

	public ConnectionManager(String url, Connection.Method method) {
		this.url = url;
		this.method = method;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public void addHeader(String key, String value){
		headers.put(key, value);
	}
	
	public Boolean getFollowRedirect() {
		return followRedirect;
	}

	public void setFollowRedirect(Boolean followRedirect) {
		this.followRedirect = followRedirect;
	}

	@Override
	public Response execute() {
		Response response = null;
		
		broadcast(StatusBar.class, null, "Connecting...");
		
		if ((Constants.PROXY_HOST != null && !Constants.PROXY_HOST.trim().isEmpty())
				|| (Constants.PROXY_PORT != null && !Constants.PROXY_PORT.trim().isEmpty()) ){
			System.setProperty("http.proxyHost", Constants.PROXY_HOST);
			System.setProperty("http.proxyPort", Constants.PROXY_PORT);
		}
		
		Map<String, String> cookies = DataUtil.getInstance().getCookies();

		for (int i = 1; i <= Constants.RETRIES; i++) {
			try {
				Connection connection = Jsoup.connect(this.url)
						.timeout(Integer.parseInt(Constants.TIME_OUT) * 1000)
						.method(this.method);
				
				if (data != null){
					connection.data(this.data);
				}
				
				if (cookies != null){
					connection.cookies(cookies);
				}
				
				for (Map.Entry<String, String> header : headers.entrySet()){
					if (header.getValue() != null){
						connection.header(header.getKey(), header.getValue());
					}
				}
				
				response = connection.execute();

				// break if no error occur
				break;
			} catch (SocketTimeoutException e) {
				// Swallow exception and try again
				log.warn("Timeout occurred " + i + " time(s)");
				
				String message = e.getMessage() + " retrying "  + i + " time(s) ... ";
				broadcast(StatusBar.class, null, message);
				response = null;
			} catch (IOException e) {
				log.error("Something went wrong with the connection..", e);
				response = null;
				
				String message = e.getMessage() + " retrying "  + i + " time(s) ... ";
				broadcast(StatusBar.class, null, message);
			}
		}
		
		broadcast(StatusBar.class, null, "Connected");

		return response;
	}
}
