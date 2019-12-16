package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.CurrentBalance;
import com.compulynx.iMbank.models.ObjResponse;

public interface CurrentBalanceDal {
	public List<CurrentBalance> getAllCurrentBalance();

	ObjResponse updateCurrentBalance(CurrentBalance currentbalance);

}
