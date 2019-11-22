package com.glogger.automation.mina;

import java.net.InetSocketAddress;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.glogger.automation.service.event.MinaMessageClient;
import com.glogger.automation.util.MinaUtil;

public class CustomMessageClient extends MinaMessageClient{

	public CustomMessageClient() {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setDefaultRemoteAddress(new InetSocketAddress("localhost", 9898));
		connector.setFilterChainBuilder(MinaUtil.getInstance().getFilterChainBuilder());
		this.setIoConnector(connector);
		bind();
	}
}
