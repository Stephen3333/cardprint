package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.UserGroupBal;
import com.compulynx.iMbank.dal.impl.UserGroupDalImpl;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.UserGroup;


@Component
public class UserGroupBalImpl implements UserGroupBal {

	@Autowired
    UserGroupDalImpl userGroupDal;

	public ObjResponse UpdateUserGroup(UserGroup userGroup) {
		return userGroupDal.UpdateUserGroup(userGroup);
	}

	public List<UserGroup> GetAllGroups( int LinkId) {
		return userGroupDal.GetGroups( LinkId);
	}

	public List<RightsDetail> GetRights() {
		return userGroupDal.GetRights();
	}

    public List<UserGroup> getActiveGroups( int groupLink) {
        return userGroupDal.getActiveGroups(groupLink);
    }

	@Override
	public List<RightsDetail> GetAllRights() {
		// TODO Auto-generated method stub
		return userGroupDal.GetAllRights();
	}

	@Override
	public List<Rights> getHeaders() {
		// TODO Auto-generated method stub
		return userGroupDal.getHeaders();
	}

	@Override
	public ObjResponse UpdateRight(RightsDetail right) {
		// TODO Auto-generated method stub
		return userGroupDal.UpdateRight(right);
	}

}
