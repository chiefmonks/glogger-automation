package com.glogger.automation.service;

import java.math.BigInteger;

import com.glogger.automation.service.event.IMessageClient;
import com.glogger.automation.service.event.IMessageServer;
import com.glogger.automation.service.event.Message;
import com.glogger.automation.service.event.MessageType;
import com.glogger.automation.util.MinaUtil;

public class MessageEnabledService implements IMessageEnabledService {
    
    protected IMessageServer messageServer;
    
    protected IMessageClient messageClient;
    
    protected Integer serviceLockTimeout = 5000;
    
    protected Integer serviceLockCheckTimeout = 1000;
    
    protected int lockRequestCount = 0;
    
    protected long lastLockRequestTimestamp = -1;
    
    /**
     * Identifier for the next {@link Message} that will be sent by this
     * service.
     */
    protected BigInteger messageNumber = BigInteger.ZERO;
    
    public MessageEnabledService(){
    	
    }
    
    public void setServiceLockTimeout(Integer serviceLockTimeout) {
        this.serviceLockTimeout = serviceLockTimeout;
    }
    
    public void setServiceLockCheckTimeout(Integer serviceLockCheckTimeout) {
        this.serviceLockCheckTimeout = serviceLockCheckTimeout;
    }
    
    public BigInteger lockService() {
        lockRequestCount++;
        lastLockRequestTimestamp = System.currentTimeMillis();
        
        return messageNumber;
    }
    
    public void unlockService() {
        if (lockRequestCount > 0) lockRequestCount--;
    }
    
    public boolean isServiceLocked() {
        // update lockRequestCount on service lock timeout
        if (lastLockRequestTimestamp > 0 && System.currentTimeMillis() - lastLockRequestTimestamp > serviceLockTimeout) {
            lockRequestCount = 0;
        }
        return lockRequestCount != 0;
    }
    
    protected synchronized void waitUntilServiceUnlocked() {
        while (isServiceLocked()) {
            try {
                this.wait(serviceLockCheckTimeout);
            }
            catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted", e);
            }
        }
    }

    public synchronized void broadcast(Class<?> messageClass, MessageType messageType, Object message) {
    	this.messageServer = MinaUtil.getInstance().getMessageServer();
        messageServer.broadcast(messageClass, messageNumber, messageType, message);
        messageNumber = messageNumber.add(BigInteger.ONE);
    }
}