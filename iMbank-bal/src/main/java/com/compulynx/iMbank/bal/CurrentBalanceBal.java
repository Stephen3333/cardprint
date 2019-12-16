package com.compulynx.iMbank.bal;

import java.util.List;



import com.compulynx.iMbank.models.CurrentBalance;
import com.compulynx.iMbank.models.ObjResponse;

public interface CurrentBalanceBal {
	public List<CurrentBalance> getAllCurrentBalance();
	ObjResponse updateCurrentBalance(CurrentBalance currentbalance);
}
