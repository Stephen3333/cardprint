package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.CardPrint;
import com.compulynx.iMbank.models.ObjResponse;


public interface CardPrintBal {
	public List<CardPrint> getAccDetails(String userId);

	public ObjResponse createUpdateCard(CardPrint cardPrint);
}
