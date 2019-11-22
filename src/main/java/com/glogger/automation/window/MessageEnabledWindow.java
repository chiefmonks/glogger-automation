package com.glogger.automation.window;

import java.util.ArrayList;
import java.util.List;

import com.glogger.automation.listener.IWndowListener;
import com.glogger.automation.service.event.IMessageClient;
import com.glogger.automation.service.event.IMessageListener;
import com.glogger.automation.service.event.Message;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.MinaUtil;

public class MessageEnabledWindow {
	
	private IMessageClient messageClient;
    
    private MessageListener messageListener = new MessageListener();
    
    protected Class<?> type;
    
    private List<IWndowListener> listeners = null;
    
    public MessageEnabledWindow(Class<?> type){
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
    
    public void addWindowListener(IWndowListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<IWndowListener>();
        }
        listeners.add(listener);
    }
    
    public void removeWindowListener(IWndowListener listener) {
        if (listeners == null) return;
        while (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
    
    public void removeAllWindowListeners() {
        if (listeners == null) return;
        listeners.clear();
    }
    
    protected void fireClockIn(Message message) {
        if (listeners == null) return;
        
        for (IWndowListener listener : listeners) {
            listener.clockIn(message);
        }
    }
    
    protected void fireLogIn(Message message) {
        if (listeners == null) return;
        
        for (IWndowListener listener : listeners) {
            listener.login(message);
        }
    }
    
    protected void fireMeal(Message message) {
        if (listeners == null) return;
        
        for (IWndowListener listener : listeners) {
            listener.meal(message);
        }
    }
    
    protected void fireLogout(Message message) {
        if (listeners == null) return;
        
        for (IWndowListener listener : listeners) {
            listener.logout(message);
        }
    }
    
    protected void fireSignout(Message message) {
        if (listeners == null) return;
        
        for (IWndowListener listener : listeners) {
            listener.signout(message);
        }
    }
    
    
    protected void onMessageReceived(Message message) {
    	Integer messageType = message.getMessageType();
        if (messageType == null) return;

        MessageType eventType = MessageType.values()[messageType];
        switch (eventType) {
	        case CLOCKIN:
	            fireClockIn(message);
	            break;
	        case MEAL:
	        	fireMeal(message);
	            break;
	        case LOGIN:
	            fireLogIn(message);
	            break;
	        case LOGOUT:
	            fireLogout(message);
	            break;
	        case SIGNOUT:
	            fireSignout(message);
	            break;
        }
    }
    
    public void addListener(Class<?> messageClass, Integer messageType, IMessageListener listener){
    	MinaUtil.getInstance().getMessageClient().addMessageListener(messageClass, messageType, listener);
    }
    
    private class MessageListener implements IMessageListener {

        public void messageReceived(Message message) {
            onMessageReceived(message);
        }
    }
}
