package com.glogger.automation.ui;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.json.domain.Activity;
import com.glogger.automation.util.DateUtil;

public class ActivityTableModel extends AbstractTableModel {
	
	private final String PATTERN = "yyyy-MM-dd hh:mm:ss a";
	
	private DateUtil dateUtil = DateUtil.getInstance();

	private static final long serialVersionUID = -660651750586192695L;
	
	private List<Activity> activities;

    public ActivityTableModel() {
    	this(null);
    }
    
    public ActivityTableModel(List<Activity> activities) {
        this.activities = activities;
    }
    
    public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

    public int getColumnCount() {
        return 5;
    }

    public int getRowCount() {
        return activities.size();
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0: return "Shift Id";
        case 1: return "Status";
        case 2: return "Start Date";
        case 3: return "End Date";
        case 4: return "Duration";
        default: return null;
        }
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        Activity activity = activities.get(rowIndex);
        Date start = dateUtil.getDateAddHour(activities.get(rowIndex).getStartDate(), Constants.REAL_DIFFERENCE);
        Date end = dateUtil.getDateAddHour(activities.get(rowIndex).getEndDate(), Constants.REAL_DIFFERENCE);
        
        String startDate = dateUtil.getDate(start, PATTERN);
		String endDate = dateUtil.getDate(end, PATTERN);

        switch (columnIndex) {
        case 0: return activity.getShiftId();
        case 1: return activity.getStatusName();
        case 2: return startDate;
        case 3: return endDate;
        case 4: return activity.getDuration();
        default: return null;
        }
    }
}