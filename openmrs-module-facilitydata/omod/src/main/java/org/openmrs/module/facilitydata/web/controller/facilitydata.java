package org.openmrs.module.facilitydata.web.controller;

import java.util.Date;

public class facilitydata {
	
	String uuid;
	String facility;
	Date fromDate;
	Date toDate;
	String valueCoded;
	Double valueNumeric;
	String question;
	String comments;
	String valueText;
	
	
	public String getValueCoded() {
		return valueCoded;
	}
	public void setValueCoded(String valueCoded) {
		this.valueCoded = valueCoded;
	}
	public Double getValueNumeric() {
		return valueNumeric;
	}
	public void setValueNumeric(Double valueNumeric) {
		this.valueNumeric = valueNumeric;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getValueText() {
		return valueText;
	}
	public void setValueText(String valueText) {
		this.valueText = valueText;
	}	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	

}
