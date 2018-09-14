package com.innowave.mahaulb.reports.manager;

import javax.persistence.Entity;

import com.innowave.mahaulb.reports.data.Base;

public class Column extends Base{
	private String title;
	private String field;
	private String dataType;
	private Boolean wantedInReport = false;

	public Column(String title, String field, String dataType) {
		this.title = title;
		this.field = field;
		this.dataType = dataType;
	}
	
	public Column() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Boolean getWantedInReport() {
		return wantedInReport;
	}

	public void setWantedInReport(Boolean wantedInReport) {
		this.wantedInReport = wantedInReport;
	}
	
	
}
