package com.glogger.automation.json.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.glogger.automation.constants.Constants;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class StatusWrapper implements Serializable{

	private static final long serialVersionUID = 7175591538885463524L;
	
	@JsonProperty(Constants.JSON_SUB_GROUP_LIST)
	private List<Status> status;

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

}