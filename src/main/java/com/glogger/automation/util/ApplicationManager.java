package com.glogger.automation.util;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationManager{// extends MessageEnabledUtil{
	
	private static final Logger log = LogManager.getLogger(ApplicationManager.class);

	private ApplicationInstanceListener subListener;
	
	private static ApplicationManager instance = new ApplicationManager();

	private ApplicationManager() {
//		super(ApplicationManager.class);
	}
	
	public static ApplicationManager getInstance() {
		return instance;
	}

	public boolean registerInstance() {
		if (!isPortAlreadyUse()){
			MinaUtil.getInstance().getMessageServer();
			fireNewInstance();
			
			return true;
		} else{
			log.info("Another instance of this application is already running.  Exiting.");
			
//			setMessageClient(MinaUtil.getInstance().getMessageClient());
			
			MinaUtil.getInstance().getMessageClient().broadcast(ApplicationManager.class, BigInteger.TEN, null, "Show current instance...");
			
			MinaUtil.getInstance().getMessageClient().removeAllListener();
			
			MinaUtil.getInstance().getMessageClient().unbind();
			
			return false;
			
		}

	}

	public void addListener(ApplicationInstanceListener listener) {
		subListener = listener;
	}

	private void fireNewInstance() {
		if (subListener != null) {
			subListener.createIntance();
		}
	}

	public interface ApplicationInstanceListener {
		public void createIntance();
	}
	
	
	private boolean isPortAlreadyUse() {
        try {
        	Socket clientSocket = new Socket(InetAddress.getByAddress(new byte[] {127, 0, 0, 1}), 9898);
        	clientSocket.close();
            return true;
        } catch (IOException e) {
        	return false;
        }
    }
}
