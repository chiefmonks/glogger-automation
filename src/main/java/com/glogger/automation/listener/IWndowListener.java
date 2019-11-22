package com.glogger.automation.listener;

import com.glogger.automation.service.event.Message;


public interface IWndowListener {

	public void login(Message message);
	
	public void clockIn(Message message);

    public void meal(Message message);
    
    public void logout(Message message);
    
    public void signout(Message message);
}
