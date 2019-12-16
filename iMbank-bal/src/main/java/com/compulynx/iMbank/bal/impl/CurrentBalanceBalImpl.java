package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.CurrentBalanceBal;
import com.compulynx.iMbank.dal.impl.CurrentBalanceDalImpl;
import com.compulynx.iMbank.models.CurrentBalance;
import com.compulynx.iMbank.models.ObjResponse;

public class CurrentBalanceBalImpl implements CurrentBalanceBal{
	
	@Autowired
    CurrentBalanceDalImpl currentbalanceDal;
	@Override
	public List<CurrentBalance> getAllCurrentBalance() {
		return currentbalanceDal.getAllCurrentBalance();
	}
	@Override
	public ObjResponse updateCurrentBalance(CurrentBalance currentbalance) {
		// TODO Auto-generated method stub
		return currentbalanceDal.updateCurrentBalance(currentbalance);
	}
}
