package com.glogger.automation.exception;

import com.glogger.automation.json.domain.Credential;

public class PasswordExpiredException extends Exception{

	private static final long serialVersionUID = -7782510946089572804L;

	private Credential credential;


	public PasswordExpiredException(Credential credential, String message, Throwable throwable){
		super(message, throwable);
		this.credential = credential;
	}
	
	public PasswordExpiredException(Credential credential, String message){
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
