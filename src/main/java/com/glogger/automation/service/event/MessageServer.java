package com.glogger.automation.service.event;

import java.math.BigInteger;


public abstract class MessageServer implements IMessageServer {
    
    
    
    @Override
    public void broadcast(Class<?> messageClass, BigInteger messageNumber, int messageType, Object message) {
        Message messageObj = new Message(messageClass, messageNumber, messageType, message);
        broadcast(messageObj);
    }
    
    @Override
    public void broadcast(Class<?> messageClass, BigInteger messageNumber, Enum<?> messageType, Object message) {
        Message messageObj = new Message(messageClass, messageNumber, messageType == null ? null : messageType.ordinal(), message);
        broadcast(messageObj);
    }
    
    public abstract void broadcast(Message message);
}
