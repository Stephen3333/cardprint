package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser {
	public int userId;
	public int respCode;
	public int userBioID;
    public String respMessage;
    public int userBranchId;
	
	public LoginUser(int userId, int respCode,String respMessage,int userBranchId) {
		super();
		this.userId = userId;
		this.respCode = respCode;
		this.respMessage = respMessage;
        this.userBranchId=userBranchId;
	}
	
	public LoginUser(int userId,int userBioID, int respCode,String respMessage) {
		super();
		this.userId = userId;
		this.userBioID = userBioID;
		this.respCode = respCode;
        this.respMessage = respMessage;
	}

	public LoginUser(int respCode,String respMessage) {
		super();
		this.respCode = respCode;
        this.respMessage = respMessage;
	}

	public LoginUser() {
		super();
	}

}
