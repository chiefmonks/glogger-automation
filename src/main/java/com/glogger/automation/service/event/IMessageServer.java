package com.glogger.automation.service.event;

import java.math.BigInteger;

public interface IMessageServer {
    
    /**
     * Broadcast the specified {@link Message} to all client.
     * 
     * @param message
     */
    public void broadcast(Message message);
    
    /**
     * Broadcast the specified <code>object</code> to all clients.
     * 
     * @param messageClass
     * @param messageType
     * @param message
     */
    public void broadcast(Class<?> messageClass, BigInteger messageNumber, int messageType, Object message);
    
    /**
     * Broadcast the specified <code>object</code> to all clients.
     * 
     * @param messageClass
     * @param messageType
     * @param message
     */
    public void broadcast(Class<?> messageClass, BigInteger messageNumber, Enum<?> messageType, Object message);
}
