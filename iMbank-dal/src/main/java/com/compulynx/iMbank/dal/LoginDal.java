package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.LoginSession;
import com.compulynx.iMbank.models.LoginUser;
import com.compulynx.iMbank.models.User;


public interface LoginDal {

	LoginUser GetUserIdManual(String userName, String password);

	LoginSession GetUserAssgnRightsList(int userId);
	
    LoginUser resetPassword(User user);

    LoginUser ForgotPassword(String userName);

	List<Branch> GetUserBranches(int userId);

}
