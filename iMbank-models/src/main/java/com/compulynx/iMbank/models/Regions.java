package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Regions {

    public int id;
    public String regionName;
    public boolean active;
    public int respCode;
    public String respMessage;
	public String createdBy;


    public Regions(int id,String regionName,String regionCode,int respCode,String respMessage,boolean active)
    {
        super();
        this.id=id;
        this.regionName=regionName;
        this.active = active;
        this.respCode=respCode;
        this.respMessage=respMessage;

    }
    public Regions(int respCode, String respMessage) {
        super();
        this.respCode = respCode;
        this.respMessage = respMessage;
    }

    public Regions()
    {

        super();
    }






}
