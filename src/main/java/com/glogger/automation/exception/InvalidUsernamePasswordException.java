package com.glogger.automation.exception;

import com.glogger.automation.json.domain.Credential;

public class InvalidUsernamePasswordException extends Exception{

	private static final long serialVersionUID = -8962669425490997455L;
	
	private Credential credential;


	public InvalidUsernamePasswordException(Credential credential, String message, Throwable throwable){
		super(message, throwable);
		this.credential = credential;
	}
	
	public InvalidUsernamePasswordException(Credential credential, String message){
		super(message);
		this.credential = credential;
	}
	
	public Credential getCredential() {
		return credential;
	}
	
	public void setCredential(Credential credential) {
		this.credential = credential;
	}
}
