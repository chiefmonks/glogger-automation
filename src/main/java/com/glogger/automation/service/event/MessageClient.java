package com.glogger.automation.service.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class MessageClient implements IMessageClient {
    private Map<Class<?>, Map<Integer, List<IMessageListener>>> listeners;
    
    public void broadcast(Class<?> messageClass, BigInteger messageNumber, MessageType messageType, Object message) {
    	Message messageObj = new Message(messageClass, messageNumber, messageType == null ? null : messageType.ordinal(), message);
        broadcast(messageObj);
    }
    
    public abstract void broadcast(Message message);
    
    @Override
    public void addMessageListener(Class<?> messageClass, Integer messageType,
                                   IMessageListener listener) {
        if (listeners == null) {
            listeners = new HashMap<Class<?>, Map<Integer,List<IMessageListener>>>();
        }
        
        Map<Integer, List<IMessageListener>> classListeners = listeners.get(messageClass);
        if (classListeners == null) {
            classListeners = new LinkedHashMap<Integer, List<IMessageListener>>();
            listeners.put(messageClass, classListeners);
        }
        
        List<IMessageListener> listenerList = classListeners.get(messageType);
        if (listenerList == null) {
            listenerList = new ArrayList<IMessageListener>();
            classListeners.put(messageType, listenerList);
        }
        
        listenerList.add(listener);
    }

    @Override
    public void removeMessageListener(Class<?> messageClass, Integer messageType,
                                      IMessageListener listener) {
        if (listeners == null) return;
        
        Map<Integer, List<IMessageListener>> classListeners = listeners.get(messageClass);
        if (classListeners == null) return;
        
        List<IMessageListener> listenerList = classListeners.get(messageType);
        if (listenerList == null) return;
        
        while (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }
    
    public void removeAllListener(){
    	if (listeners == null) return;
        listeners.clear();
    }
    
    protected List<IMessageListener> getListeners(Class<?> messageClass, Integer messageType) {
        if (listeners == null) return null;
        
        Map<Integer, List<IMessageListener>> classListeners = listeners.get(messageClass);
        if (classListeners == null) return null;
        
        return classListeners.get(messageType);
    }

    protected void fireMessage(Message message) {
        // send message to listeners of the message type
        List<IMessageListener> listeners = getListeners(message.getMessageClass(), message.getMessageType());
        if (listeners != null) {
            for (IMessageListener listener : listeners) {
                listener.messageReceived(message);
            }
        }
        
        // send message to listeners of all message types
        listeners = getListeners(message.getMessageClass(), null);
        if (listeners != null) {
            for (IMessageListener listener : listeners) {
                listener.messageReceived(message);
            }
        }
    }
}
