package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.DashBoard;

public interface DashBoardDal {
	 List<DashBoard> GetDashBoardCountDetail();
	 List<DashBoard> GetTransChartDetail();

}
