package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.CardPrintBal;
import com.compulynx.iMbank.dal.impl.CardPrintDalImpl;
import com.compulynx.iMbank.models.CardPrint;
import com.compulynx.iMbank.models.ObjResponse;



public class CardPrintBalImp implements CardPrintBal {
	@Autowired
	CardPrintDalImpl cardprintDal;

	@Override
	public List<CardPrint> getAccDetails(String userId) {
		// TODO Auto-generated method stub
		return cardprintDal.getAccDetails(userId);
	}

	
	@Override
	public ObjResponse createUpdateCard(CardPrint cardPrint) {
		// TODO Auto-generated method stub
		return cardprintDal.createUpdateCard(cardPrint);
	}
}
