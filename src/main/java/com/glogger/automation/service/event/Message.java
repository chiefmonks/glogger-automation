package com.glogger.automation.service.event;

import java.io.Serializable;
import java.math.BigInteger;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Class<?> messageClass;
    private BigInteger messageNumber;
    private Integer messageType;
    private Object message;
    
    public Message(Class<?> messageClass, BigInteger messageNumber, Integer messageType, Object message) {
        this.messageClass = messageClass;
        this.messageNumber = messageNumber;
        this.messageType = messageType;
        this.message = message;
    }
    
    public Class<?> getMessageClass() {
        return messageClass;
    }
    
    public BigInteger getMessageNumber() {
        return messageNumber;
    }
    
    public Integer getMessageType() {
        return messageType;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getMessage() {
        return (T) message;
    }
    
    public boolean isValidSequence(BigInteger messageNumber) {
        return messageNumber.equals(this.messageNumber);
    }
}
