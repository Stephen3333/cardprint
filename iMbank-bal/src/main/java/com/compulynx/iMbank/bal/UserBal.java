/**
 * 
 */
package com.compulynx.iMbank.bal;



import java.util.List;

import com.compulynx.iMbank.models.LoginTypes;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;

public interface UserBal {

	ObjResponse UpdateUser(User user);

	List<User> GetAllUsers( int LinkId);
	List<User> GetAllUsers();

	List<User> GetClasses();

	List<User> GetQuestions();

	User GetUserById(int userId);

    public List<LoginTypes> getLoginTypesList();
    public ObjResponse resetFailAttempts(User user);
	List<User> getUserAttempts();
   
}
