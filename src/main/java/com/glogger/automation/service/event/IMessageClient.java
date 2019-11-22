package com.glogger.automation.service.event;

public interface IMessageClient {
	
	public void broadcast(Message message);

    public void addMessageListener(Class<?> messageClass, Integer eventType, IMessageListener listener);
    
    public void removeMessageListener(Class<?> messageClass, Integer eventType, IMessageListener listener);
    
    public void bind();
    
    public void unbind();
}
