package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginSession {
	public int sessionId;
	public int linkId;
	public int userClassId;
	public int userGroupId;
	public String linkName;
	public String sessionName;
	public String sessionFullName;
	public String linkExtInfo;
	public Boolean isFirstLogin;

	public String branchName;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public void setLinkExtInfo(String linkExtInfo) {
		this.linkExtInfo = linkExtInfo;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public void setSessionFullName(String sessionFullName) {
		this.sessionFullName = sessionFullName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public List<Rights> rightsHeaderList;
	public int respCode;

	public LoginSession() {
		super();
	}

	public void setRightsList(List<Rights> rightsList) {
		this.rightsHeaderList = rightsList;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public void setUserClassID(int userClassID) {
		this.userClassId = userClassID;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public LoginSession(int respCode) {
		super();
		this.respCode = respCode;
	}

}
