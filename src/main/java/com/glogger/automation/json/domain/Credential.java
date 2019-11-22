package com.glogger.automation.json.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Credential implements Serializable{

	private static final long serialVersionUID = -6259533377677015498L;
	
	@JsonProperty("response")
	private Boolean isAuthenticated;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("empId")
	private String employeeId;

	
	public Credential(Boolean isAuthenticated, String employeeId){
		this.isAuthenticated = isAuthenticated;
		this.employeeId = employeeId;
	}
	
	public Credential(){
		
	}

	public Boolean getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "Credential [isAuthenticated=" + isAuthenticated + ", message="
				+ message + ", employeeId=" + employeeId + "]";
	}
}