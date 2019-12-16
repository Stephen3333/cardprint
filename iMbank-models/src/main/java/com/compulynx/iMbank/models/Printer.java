package com.compulynx.iMbank.models;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Printer {

    public int id;
    public String serialNumber;
    public String ipAddress;
    public int branchId;
    public String branchName;
    public boolean status;
    public String createdOn;
    public String createdBy;


    public int respCode;
    public String respMessage;
	public int counter;


    public Printer() {
        super();
    }

    public Printer(int id, String serialNumber, int branchId, String branchName,
                    boolean status, String createdBy, String createdOn, int respCode, String respMessage) {
        super();
        this.id = id;
        this.serialNumber = serialNumber;
        this.branchId = branchId;
        this.branchName = branchName;
        this.status = status;
        this.createdBy=createdBy;
        this.createdOn=createdOn;
        this.respCode = respCode;
        this.respMessage = respMessage;
    }

    public Printer(int respCode, String respMessage) {
        super();
        this.respCode = respCode;
        this.respMessage = respMessage;
    }


}
