package com.compulynx.iMbank.models;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardReject {
	public int id;
	public int cardNum;

	//@JsonIgnore
	public String rejectedBy;
	public int branchId;
	public String reason;
	public String rejectedOn;
	
	public CardReject(int id,int cardNum, String rejectedBy, int branchId, String reason,
			 String rejectedOn) {
		super();
		this.id = id;
		this.cardNum = cardNum;
		this.rejectedBy = rejectedBy;
		this.branchId = branchId;
		this.reason = reason;
		this.rejectedOn = rejectedOn;
	}

	public CardReject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
