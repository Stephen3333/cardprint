package com.compulynx.iMbank.bal;

import com.compulynx.iMbank.models.LoginSession;
import com.compulynx.iMbank.models.LoginUser;
import com.compulynx.iMbank.models.User;




public interface LoginBal {

	LoginUser ValidateManualLogin(String userName, String password);

	LoginUser resetPassword(User user);

	LoginSession GetLoginSession(int userId);
	
    LoginUser ForgotPassword(String userName);
}
