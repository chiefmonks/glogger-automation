package com.glogger.automation.service.event;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaMessageClient extends MessageClient implements IoHandler {
    private final static Logger log = LoggerFactory.getLogger(MinaMessageClient.class);
    
    protected IoConnector ioConnector;
    
    public synchronized void broadcast(Message  message) {
    	ioConnector.broadcast(message);
    }
    
    public void setIoConnector(IoConnector ioConnector) {
        if (this.ioConnector != null) {
            this.ioConnector.setHandler(null);
            this.ioConnector.dispose();
        }
        
        this.ioConnector = ioConnector;
        
        this.ioConnector.setHandler(this);
    }
    
    @Override
    public void bind() {
        final ConnectFuture future = ioConnector.connect();
        
        // TODO: Add timeout support here
        future.awaitUninterruptibly();
    }
    
    @Override
    public void unbind() {
        if (ioConnector == null) return;
        
        ioConnector.dispose();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        log.warn("Exception thrown", throwable);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof Message) {
            fireMessage((Message) message);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
//    	if (message instanceof Message) {
//    		session.write(message);
//            fireMessage((Message) message);
//        }
    }

    @Override
    public void sessionClosed(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void sessionCreated(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sessionOpened(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
//		session.closeNow();
	}
    
    
}
