package com.glogger.automation.listener;

import com.glogger.automation.service.event.Message;



public interface ApplicationListener {

	public void initialized(Message message);
}
