package com.glogger.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtil {
	
	public static PropertyUtil instance;
	
	private final String configFile = "config.properties";
	
	private final String dataFile = "data.properties";
	
	private final Properties properties;
	
	public static final String START = "start";
	
	public static final String MEAL = "meal";
	
	public static final String IN = "in";
	
	public static final String END = "end";
	
	public static final String OUT = "out";
	
	public static final String LOG = "log";
	
	private PropertyUtil() {
		properties = loadProperties(configFile);
		
		File file = new File(dataFile);
		if (!file.exists()) createDataFileProperty();
	}
	
	public static PropertyUtil getInstance(){
		if (instance == null)
			instance = new PropertyUtil();
		return instance;
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	private void write(Properties props, String outFilename){
		OutputStream output = null;

		try {

			output = new FileOutputStream(outFilename);
			// save properties to project root folder
			props.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	private void createDataFileProperty(){
		Properties prop = new Properties();
		// set the properties value
		prop.setProperty(START, "");
		prop.setProperty(MEAL, "");
		prop.setProperty(IN, "");
		prop.setProperty(END, "");
		prop.setProperty(LOG, "");
		
		write(prop, dataFile);
	}
	
	private Properties loadProperties(String filename){
		Properties props = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(filename);

			// load a properties file
			props.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}
	
	public void saveDataFileProperty(String key, Object value){
		File file = new File(dataFile);
		if (!file.exists()) createDataFileProperty();
		
		Properties props = loadProperties(dataFile);
		props.setProperty(key, String.valueOf(value));
		
		write(props, dataFile);
	}
	
	public void saveDataFileProperty(Properties properties){
		File file = new File(dataFile);
		if (!file.exists()) createDataFileProperty();
		
		Properties props = loadProperties(dataFile);
		props.putAll(properties);
		
		write(props, dataFile);
	}
	
	public Properties getDataFileProperty(){
		return loadProperties(dataFile);
	}
	
	public void clearDataFileProperty(){
		createDataFileProperty();
	}
}
