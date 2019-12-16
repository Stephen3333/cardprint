/**
 * 
 */
package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	

	public int userId;
	public String userName;
	public String userFullName;
	public String userPwd;
	public int userGroupId;
	public String userGroupName;
	public String userEmail;
	public String userPhone;
	public String userSecretQuestion;
	public int userSecretQuestionId;
	public String userSecretAns;
	public int userBioLogin;
	public long userLinkedID;
	public long userBioID;
    public int userIrisId;
	public boolean active;
	public int createdBy;
	public String createdOn;
	public int respCode;
	public int classId;
    public String payrollNo;
    //public String pfId;
    public Date passwordExpiry;
    //public List<Branch> branchesList;
    public int branchId;
    public int counter;
    public int pwdAttempts;
    public boolean isLocked; 
    public String locked;
    public String firstName;
    public String middleName;
    public String lastName;


	public String className;
	
	public int getPwdAttempts() {
		return pwdAttempts;
	}

	public void setPwdAttempts(int pwdAttempts) {
		this.pwdAttempts = pwdAttempts;
	}
	public User(String userSecretQuestion, int userSecretQuestionId) {
		super();
		this.userSecretQuestion = userSecretQuestion;
		this.userSecretQuestionId = userSecretQuestionId;
	
	}

	public User(int classId, String className, int respCode) {
		super();
	
		this.respCode = respCode;
		this.classId = classId;
		this.className = className;
	}

	public User(int respCode) {
		super();
		this.respCode = respCode;
	}

	public User(int userId,String userName, String userFullName, String userPwd,
			int userGroupId, String userEmail, String userPhone,
			int userSecretQuestionId, String userSecretAns,
			int userBioLogin, long userLinkedID, long userBioID,
			boolean active, int createdBy, int respCode,int userIrisId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.userGroupId = userGroupId;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userSecretQuestionId = userSecretQuestionId;
		this.userSecretAns = userSecretAns;
		this.userBioLogin = userBioLogin;
		this.userLinkedID = userLinkedID;
		this.userBioID = userBioID;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
        this.userIrisId = userIrisId;

	}
    /*public User(int userId,String userName, String userFullName, String userPwd,
                int userGroupId, String userEmail, String userPhone,
                int userSecretQuestionId, String userSecretAns,
                int userBioLogin, long userLinkedID, long userBioID,
                boolean active, int createdBy, int respCode,int userIrisId,String payrollNo,String pfId,Date passwordExpiry,List<Branch> branchesList,int branchId) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userFullName = userFullName;
        this.userPwd = userPwd;
        this.userGroupId = userGroupId;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userSecretQuestionId = userSecretQuestionId;
        this.userSecretAns = userSecretAns;
        this.userBioLogin = userBioLogin;
        this.userLinkedID = userLinkedID;
        this.userBioID = userBioID;
        this.active = active;
        this.createdBy = createdBy;
        this.respCode = respCode;
        this.userIrisId = userIrisId;
        this.payrollNo=payrollNo;
        this.pfId=pfId;
        this.branchId=branchId;
        this.branchesList=new ArrayList<Branch>();
        this.passwordExpiry=passwordExpiry;
    }*/

	public User(int userId,String userName, String userFullName, String userPwd,
			int userGroupId, String userEmail, String userPhone,
			int userSecretQuestionId, String userSecretAns,
			int userBioLogin, long userLinkedID, long userBioID,
			boolean active, int createdBy, int respCode, String userGroupName,int userIrisId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.userGroupId = userGroupId;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userSecretQuestionId = userSecretQuestionId;
		this.userSecretAns = userSecretAns;
		this.userBioLogin = userBioLogin;
		this.userLinkedID = userLinkedID;
		this.userBioID = userBioID;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.userGroupName = userGroupName;
        this.userIrisId = userIrisId;
	}
	
	public User() {
		super();
	}

}
