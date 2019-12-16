package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DashBoard {

	public int detailCount;
	public String detailDescription;
    public String respMessage;
	public int respCode;
	
	public DashBoard(int respCode,String respMessage) {
		super();
		this.respCode = respCode;
        this.respMessage = respMessage;
	}
	
	public DashBoard() {
		super();
	}

	public DashBoard(int detailCount, String detailDescription, int respCode) {
		super();
		this.detailCount = detailCount;
		this.detailDescription = detailDescription;
		this.respCode = respCode;
	}
}
