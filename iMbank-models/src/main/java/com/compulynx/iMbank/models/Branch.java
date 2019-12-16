package com.compulynx.iMbank.models;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {

    public int branchId;
    public String branchName;
    public String branchCode;
    public String createdOn;
    public String createdBy;
    public String branchAddress;
    public int respCode;
    public String respMessage;
    public int regionId;
    public String regionName;
    public boolean status;
    public boolean isChecked;
    public int counter;


    public Branch() {
        super();
    }

    public Branch(int respCode, String respMessage) {
        super();
        this.respCode = respCode;
        this.respMessage = respMessage;
    }

    public Branch(int branchId, String branchName,String branchCode, String createdOn, String createdBy,String branchAddress,int regionId,String regionName, int respCode, String respMessage,boolean status) {
        super();
        
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchCode = branchCode;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.branchAddress = branchAddress;
        this.regionId = regionId;
        this.regionName = regionName;
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.status=status;

        //this.
    }
    public Branch(int branchId, String branchName,int respCode)
    {
        super();
        
        this.branchId = branchId;
        this.branchName = branchName;
        this.respCode = respCode;

    }
    public Branch(int branchId, String branchName, boolean isChecked) {
        super();
        this.branchId = branchId;
        this.branchName = branchName;
        this.isChecked = isChecked;
    }
}
