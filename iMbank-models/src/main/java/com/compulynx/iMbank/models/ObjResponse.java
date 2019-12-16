package com.compulynx.iMbank.models;

public class ObjResponse {
	public int respCode;
	public String respMessage;


    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public ObjResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	public ObjResponse(int respCode) {
		super();
		this.respCode = respCode;
	}


    public ObjResponse() {
        super();
    }
}
