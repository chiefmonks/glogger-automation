package com.glogger.automation.json.domain;

import java.io.Serializable;
import java.util.Date;

public class Glogger implements Serializable{

	private static final long serialVersionUID = 7294422724777995995L;

	private Date date;
	
	private com.glogger.automation.constants.Status status;
	
	public Glogger(Date date, com.glogger.automation.constants.Status status){
		this.date = date;
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public com.glogger.automation.constants.Status getStatus() {
		return status;
	}

	public void setStatus(com.glogger.automation.constants.Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Glogger [date=" + date + ", status=" + status + "]";
	}
}
