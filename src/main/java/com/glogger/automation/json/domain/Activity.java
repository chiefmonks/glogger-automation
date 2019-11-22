package com.glogger.automation.json.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Activity implements Serializable{
	
	private static final long serialVersionUID = 4741477592025230628L;

	@JsonProperty("log_id")
	private Long logId;
	
	@JsonProperty("sta_status")
	private String statusName;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("log_start_date")
	private Date startDate;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("log_end_date")
	private Date endDate;
	
	@JsonProperty("log_duration")
	private String duration;
	
	@JsonProperty("shift_id")
	private Long shiftId;
	
	@JsonProperty("log_ip")
	private String ip;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Long getShiftId() {
		return shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "History [logId=" + logId + ", statusName=" + statusName
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", duration=" + duration + ", shiftId=" + shiftId + ", ip="
				+ ip + "]";
	}
}
