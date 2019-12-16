package com.compulynx.iMbank.bal.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.LoginBal;
import com.compulynx.iMbank.dal.LoginDal;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.LoginSession;
import com.compulynx.iMbank.models.LoginUser;
import com.compulynx.iMbank.models.User;

import java.util.List;

@Component
public class LoginBalImpl implements LoginBal {

	@Autowired
    LoginDal loginDal;
	
	public LoginUser ValidateManualLogin(String userName, String password) {
		return loginDal.GetUserIdManual(userName, password);
	}

    public LoginUser resetPassword(User user) {
        return loginDal.resetPassword(user);
    }

   
    public LoginUser ForgotPassword(String userName)
    {
        return loginDal.ForgotPassword(userName);
    }

	public LoginSession GetLoginSession(int userId) {
		return loginDal.GetUserAssgnRightsList(userId);
	}
    public List<Branch> GetUserBranches(int userId) {
        return loginDal.GetUserBranches(userId);
    }

}
