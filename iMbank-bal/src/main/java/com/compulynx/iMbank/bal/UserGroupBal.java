package com.compulynx.iMbank.bal;



import java.util.List;

import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.UserGroup;


public interface UserGroupBal {
	
	ObjResponse UpdateUserGroup(UserGroup userGroup);
	
	List<UserGroup> GetAllGroups(int LinkId);
	 List<RightsDetail> GetRights();
		public List<RightsDetail> GetAllRights();
    public List<UserGroup> getActiveGroups( int groupLink);
    List<Rights> getHeaders();
    ObjResponse UpdateRight(RightsDetail right) ;
}
