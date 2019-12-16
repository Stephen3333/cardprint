package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroup {
	public int groupId;
	public String groupName;
	public String groupCode;
	public int groupClassId;
	public int groupLinkId;
	public boolean active;
	public int createdBy;
	public int respCode;
    public String respMessage;
	public List<RightsDetail> rights;
	public int counter;
	
	public UserGroup(int respCode,String respMessage) {
		super();
		this.respCode = respCode;
        this.respMessage = respMessage;
	}

	public UserGroup(int groupId, String groupCode,String groupName,int groupClassID, int groupLinkID,
			boolean active, int createdBy, int respCode) {
		super();
		this.groupId = groupId;
		this.groupCode = groupCode;
		this.groupName = groupName;
		this.groupClassId = groupClassID;
		this.groupLinkId = groupLinkID;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	public UserGroup() {
		super();
	}

}
