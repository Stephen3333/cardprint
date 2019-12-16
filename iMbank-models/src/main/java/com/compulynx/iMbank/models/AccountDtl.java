/**
 * 
 */
package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDtl {
	
	public int id;
	public String customerId;
	public String firstName;
	public String lastName;
    public String middleName;
	public String address;
	public String postalCode;
	public String mobile;
	public String email;
	public int respCode;
    public String respMessage;
	public String createdBy;
    public String supplierCode;
    public String curCode;
    public String accNumber;
    public String accName;
    public boolean status;
	
	public  AccountDtl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountDtl(int respCode, String respMessage, String accName) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.accName = accName;
	}
    public AccountDtl(String createdBy, String customerId,String middleName, String firstName, String lastName, String address, String postalCode, String mobile, String email, int respCode, String respMessage ,String bioId, int id,String supplierCode) {
        this.createdBy = createdBy;
        this.customerId = customerId;
        this.middleName = middleName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.mobile = mobile;
        this.email = email;
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.id = id;
        this.supplierCode=supplierCode;
    }

    public AccountDtl(int respCode,String respMessage) {
		super();
		this.respCode = respCode;
        this.respMessage= respMessage;
	}
}
