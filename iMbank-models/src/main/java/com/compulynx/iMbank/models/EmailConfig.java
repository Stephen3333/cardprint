package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailConfig {
 public int id;
 public String hostName;
 public String port;
 public String userName;
 public String password;
 public String email;
 public String timeout;
 
 public EmailConfig(int id, String hostName, String port, String userName,
		String password, String email, String timeout) {
	super();
	this.id = id;
	this.hostName = hostName;
	this.port = port;
	this.userName = userName;
	this.password = password;
	this.email = email;
	this.timeout = timeout;
}

public EmailConfig() {
	super();
	// TODO Auto-generated constructor stub
}

}
