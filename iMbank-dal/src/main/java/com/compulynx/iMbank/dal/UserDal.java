
package com.compulynx.iMbank.dal;



import java.util.List;

import com.compulynx.iMbank.models.LoginTypes;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;


public interface UserDal {
	boolean checkUserName(String userName);

	ObjResponse UpdateUser(User user);

	List<User> GetAllUsers( int LinkId);
	List<User> GetAllUsers();

	List<User> GetClasses();

	List<User> GetQuestions();

	User GetUserById(int userId);

    public List<LoginTypes> getLoginTypesList();
    
    List<User> getUserAttempts();
    ObjResponse resetFailAttempts(User user);

   
}
