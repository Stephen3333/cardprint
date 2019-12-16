package com.compulynx.iMbank.dal;



import java.util.List;

import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.UserGroup;

public interface UserGroupDal {
	
	ObjResponse UpdateUserGroup(UserGroup group);
	
	//boolean checkGroupByName(String groupName, int groupClass, int groupLink);
	
	List<UserGroup> GetGroups(int groupLink);
	
	UserGroup GetGroupById(int groupId);
	
	List<RightsDetail> GetRights();

    public List<UserGroup> getActiveGroups(int groupLink);
	public List<RightsDetail> GetAllRights();
	List<Rights> getHeaders();
	 ObjResponse UpdateRight(RightsDetail right) ;

}	
