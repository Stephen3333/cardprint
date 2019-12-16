package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.DashBoard;

public interface DashBoardBal {
	 List<DashBoard> GetDashBoardCountDetail();
	 List<DashBoard> GetTransChartDetail();

}
