package com.glogger.automation.service.event;

import java.io.IOException;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaMessageServer extends MessageServer implements IoHandler {
    private final static Logger log = LoggerFactory.getLogger(MinaMessageServer.class);
    
    protected IoAcceptor ioAcceptor;
    
    public synchronized void broadcast(Message  message) {
        ioAcceptor.broadcast(message);
    }
    
    public void setIoAcceptor(IoAcceptor ioAcceptor) {
        if (this.ioAcceptor != null) {
            this.ioAcceptor.setHandler(null);
            this.ioAcceptor.dispose();
        }
        
        this.ioAcceptor = ioAcceptor;
    }
    
    
    public void bind() throws IOException {
        if (ioAcceptor == null) return;
        
        ioAcceptor.setHandler(this);
        
        ioAcceptor.bind();
    }
    
    public void unbind() {
        if (ioAcceptor != null) {
            ioAcceptor.unbind();
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        log.warn("Exception thrown", throwable);
//        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.debug("Received message {}", message);
        if (message instanceof Message){
        	broadcast((Message) message);
//        	Message message_ = (Message) message;
//        	broadcast(message_.getClass(), message_.getMessageNumber(), message_.getMessageType(), message_);
        }
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.debug("Session closed {}", session);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
    	
//    	SocketAddress remoteAddress = session.getRemoteAddress();
//        server.addClient(remoteAddress);
        
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("Session has gone idle {} with status {}", new Object[]{session, status});
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("Client connected: {}", session.getLocalAddress());
    }

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
//		session.closeNow();
	}
}
