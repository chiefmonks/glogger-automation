package com.glogger.automation.json.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Status implements Serializable{

	private static final long serialVersionUID = -1713179360874418850L;
	
	@JsonProperty("sub_id")
	private Integer subGroupId;
	
	@JsonProperty("sub_name")
	private String subGroupName;
	
	@JsonProperty("sta_id")
	private Integer statusId;
	
	@JsonProperty("sta_name")
	private String statusName;

	public Integer getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}

	public String getSubGroupName() {
		return subGroupName;
	}

	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Override
	public String toString() {
		return "Status [subGroupId=" + subGroupId + ", subGroupName="
				+ subGroupName + ", statusId=" + statusId + ", statusName="
				+ statusName + "]";
	}
}