package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrinterType {
	
	public int id;
	public String printerName;
	public String description;
	public int counter;

	public PrinterType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PrinterType(int id, String printerName, String description) {
		super();
		this.id = id;
		this.printerName = printerName;
		this.description = description;
	}
	
}
