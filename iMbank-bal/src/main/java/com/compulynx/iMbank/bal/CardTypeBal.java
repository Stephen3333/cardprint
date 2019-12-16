package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.PrinterType;

public interface CardTypeBal {
	public ObjResponse createUpdateCardType(CardType cardtype);
	public List<CardType> getAllCardType();
	public List<PrinterType> getAllPrinterType();
	public ObjResponse createUpdatePrinterType(PrinterType printertype);
}
