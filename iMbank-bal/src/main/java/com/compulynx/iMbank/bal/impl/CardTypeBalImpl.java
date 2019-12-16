package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.CardTypeBal;
import com.compulynx.iMbank.dal.impl.CardTypeDalImpl;
import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.PrinterType;



public class CardTypeBalImpl implements CardTypeBal{
	@Autowired
    CardTypeDalImpl cardtypeDal;
	public ObjResponse createUpdateCardType(CardType cardtype) {
        return cardtypeDal.createUpdateCardType(cardtype);
    }
	public List<CardType> getAllCardType() {

        return cardtypeDal.getAllCardType();
    }
	public List<PrinterType> getAllPrinterType() {
		// TODO Auto-generated method stub
		return cardtypeDal.getAllPrinterType();
	}
	public ObjResponse createUpdatePrinterType(PrinterType printertype) {
		// TODO Auto-generated method stub
		return cardtypeDal.createUpdatePrinterType(printertype);
	}

}
