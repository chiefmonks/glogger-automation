package com.glogger.automation.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.glogger.automation.service.event.MinaMessageServer;
import com.glogger.automation.util.MinaUtil;

public class CustomMessageServer extends MinaMessageServer{

	public CustomMessageServer() {
		
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setDefaultLocalAddress(new InetSocketAddress("localhost", 9898));
		acceptor.setReuseAddress(true);
		acceptor.setFilterChainBuilder(MinaUtil.getInstance().getFilterChainBuilder());
		this.setIoAcceptor(acceptor);
		
		try {
			bind();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
