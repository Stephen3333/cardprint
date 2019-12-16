package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {

	public int id;
	public String name;
	public String reason;
	public String accNum;
	public String createdBy;
	public String createdOn;
	
	public Activity(int id, String name, String reason, String accNum,
			String createdBy, String createdOn) {
		super();
		this.id = id;
		this.name = name;
		this.reason = reason;
		this.accNum = accNum;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public Activity() {
		super();
		
	}
	
	
}
