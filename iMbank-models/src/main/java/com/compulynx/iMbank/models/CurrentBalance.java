package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentBalance {
	public int id;
	public int branchId;
	public String  currentBalance;
	public String lastUpdate;
	
	public CurrentBalance(int id, int branchId, String currentBalance,
			String lastUpdate) {
		super();
		this.id = id;
		this.branchId = branchId;
		this.currentBalance = currentBalance;
		this.lastUpdate = lastUpdate;
	}

	public CurrentBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
