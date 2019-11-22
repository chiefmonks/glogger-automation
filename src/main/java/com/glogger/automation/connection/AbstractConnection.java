package com.glogger.automation.connection;

import java.math.BigInteger;

import org.jsoup.Connection.Response;

import com.glogger.automation.service.event.IMessageServer;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.MinaUtil;

public abstract class AbstractConnection {
	
	protected BigInteger messageNumber = BigInteger.ZERO;
	
	public abstract Response execute();
	
	public synchronized void broadcast(Class<?> messageClass, MessageType messageType, Object message) {
		IMessageServer messageServer = MinaUtil.getInstance().getMessageServer();
        messageServer.broadcast(messageClass, messageNumber, messageType, message);
        messageNumber = messageNumber.add(BigInteger.ONE);
    }
}
