package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.DashBoardBal;
import com.compulynx.iMbank.dal.impl.DashBoardDalImpl;
import com.compulynx.iMbank.models.DashBoard;

public class DashBoardBalImpl implements DashBoardBal{

	@Autowired
	DashBoardDalImpl dashBoardDal;
	
	public List<DashBoard> GetDashBoardCountDetail() {
	
		return dashBoardDal.GetDashBoardCountDetail();
	}

	@Override
	public List<DashBoard> GetTransChartDetail() {
		return dashBoardDal.GetTransChartDetail();
	}

}
