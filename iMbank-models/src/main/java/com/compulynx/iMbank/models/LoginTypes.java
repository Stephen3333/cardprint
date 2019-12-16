package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginTypes {

    public int id;
    public String name;
    public boolean value;
    public int respCode;
    public String respMessage;


    public LoginTypes() {
        super();
    }

    public LoginTypes(int id, String respMessage, int respCode, boolean value, String name) {
        this.id = id;
        this.respMessage = respMessage;
        this.respCode = respCode;
        this.value = value;
        this.name = name;
    }

    public LoginTypes(int respCode,String respMessage) {
        super();
        this.respCode = respCode;
        this.respMessage= respMessage;
    }

}
