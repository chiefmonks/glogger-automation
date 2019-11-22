package com.glogger.automation.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.glogger.automation.service.event.IMessageClient;
import com.glogger.automation.service.event.IMessageListener;
import com.glogger.automation.service.event.Message;
import com.glogger.automation.service.event.MessageType;

public class MessageEnabledUtil {
	
    protected BigInteger messageNumber = BigInteger.ZERO;
	
	private IMessageClient messageClient;
    
    private MessageListener messageListener = new MessageListener();
    
    protected Class<?> type;
    
    private List<IMessageListener> listeners = null;
    
    public MessageEnabledUtil(Class<?> type){
    	this.type = type;
    }
    
    public void setMessageClient(IMessageClient messageClient) {
    	if (this.messageClient != null) {
            this.messageClient.removeMessageListener(type, null, messageListener);
        }

        this.messageClient = messageClient;
        
        if (messageClient != null) {
            messageClient.addMessageListener(type, null, messageListener);
        }
	}
    
    public void addWindowListener(IMessageListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<IMessageListener>();
        }
        listeners.add(listener);
    }
    
    public void removeWindowListener(IMessageListener listener) {
        if (listeners == null) return;
        while (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
    
    public void removeAllWindowListeners() {
        if (listeners == null) return;
        listeners.clear();
    }
    
    protected void fireInitialized(Message message) {
        if (listeners == null) return;
        
        for (IMessageListener listener : listeners) {
            listener.messageReceived(message);
        }
    }
    
    public void addListener(Class<?> messageClass, Integer messageType, IMessageListener listener){
    	MinaUtil.getInstance().getMessageClient().addMessageListener(messageClass, messageType, listener);
    }
    
    public synchronized void broadcast(Class<?> messageClass, MessageType messageType, Object message) {
    	MinaUtil.getInstance().getMessageClient().broadcast(messageClass, messageNumber, messageType, message);
        messageNumber = messageNumber.add(BigInteger.ONE);
    }
    
    private class MessageListener implements IMessageListener {

        public void messageReceived(Message message) {
            fireInitialized(message);
        }
    }
}
