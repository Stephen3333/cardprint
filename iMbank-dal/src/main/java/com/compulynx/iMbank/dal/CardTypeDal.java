package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.PrinterType;

public interface CardTypeDal {
	
	public ObjResponse createUpdateCardType(CardType cardtype);
	public List<CardType> getAllCardType();
	public List<PrinterType> getAllPrinterType();
	public ObjResponse createUpdatePrinterType(PrinterType printertype);
}
