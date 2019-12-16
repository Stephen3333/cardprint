/**
 * 
 */
package com.compulynx.iMbank.bal.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.UserBal;
import com.compulynx.iMbank.dal.impl.UserDalImpl;
import com.compulynx.iMbank.models.LoginTypes;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;



import java.util.List;


@Component
public class UserBalImpl implements UserBal {

	@Autowired
    UserDalImpl userDal;

	public ObjResponse UpdateUser(User user) {
		return userDal.UpdateUser(user);
	}
	
	public List<User> GetAllUsers( int LinkId) {
		return userDal.GetAllUsers( LinkId);
	}

	public List<User> GetClasses() {
		return userDal.GetClasses();
	}

	public List<User> GetQuestions() {
		return userDal.GetQuestions();
	}

	
	public User GetUserById(int userId) {
		return userDal.GetUserById(userId);
	}

	
    public List<LoginTypes> getLoginTypesList() {
        return userDal.getLoginTypesList();
    }
  
	public List<User> GetAllUsers() {
		// TODO Auto-generated method stub
		return userDal.GetAllUsers();
	}
	

	public ObjResponse resetFailAttempts(User user) {
		// TODO Auto-generated method stub
		return userDal.resetFailAttempts(user);
	}

	public ObjResponse unlockUser(User user) {
		// TODO Auto-generated method stub
		return userDal.unlockUser(user);
	}

	
	public List<User> getUserAttempts() {
		// TODO Auto-generated method stub
		return userDal.getUserAttempts();
	}
}
