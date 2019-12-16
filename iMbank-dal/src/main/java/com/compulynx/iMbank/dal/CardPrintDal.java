package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.CardPrint;
import com.compulynx.iMbank.models.ObjResponse;



public interface CardPrintDal {

	List<CardPrint> getAccDetails(String userId);

	ObjResponse createUpdateCard(CardPrint cardPrint);
}
