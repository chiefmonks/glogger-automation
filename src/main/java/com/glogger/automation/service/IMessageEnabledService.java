package com.glogger.automation.service;

import java.math.BigInteger;


public interface IMessageEnabledService {
    public BigInteger lockService();
    
    public void unlockService();
    
    public boolean isServiceLocked();
}