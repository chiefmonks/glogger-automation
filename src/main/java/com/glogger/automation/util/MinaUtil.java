package com.glogger.automation.util;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;

import com.glogger.automation.mina.CustomMessageClient;
import com.glogger.automation.mina.CustomMessageServer;
import com.glogger.automation.mina.FilterChainBuilder;
import com.glogger.automation.service.event.MinaMessageClient;
import com.glogger.automation.service.event.MinaMessageServer;

public class MinaUtil {

	private static final MinaUtil instance = new MinaUtil();
	
	private DefaultIoFilterChainBuilder filterChainBuilder;
	
	private static final MinaMessageServer messageServer = new CustomMessageServer();
	
	private static final  MinaMessageClient messageClient = new CustomMessageClient();
	
	private MinaUtil() {

	}

	public static MinaUtil getInstance() {
		return instance;
	}
	
	public DefaultIoFilterChainBuilder getFilterChainBuilder(){
		if (this.filterChainBuilder == null)
			this.filterChainBuilder = new FilterChainBuilder();
		
		return this.filterChainBuilder;
	}
	
	public MinaMessageServer getMessageServer() {
		return messageServer;
	}
	
	public MinaMessageClient getMessageClient() {
		return messageClient;
	}
}
