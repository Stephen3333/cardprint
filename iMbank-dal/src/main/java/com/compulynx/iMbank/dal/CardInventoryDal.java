package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CardReject;
import com.compulynx.iMbank.models.ObjResponse;


public interface CardInventoryDal {
	public ObjResponse createUpdateCardInventory(CardInventory cardinventory);
	public List<CardInventory> getAllCardInventory();
	public ObjResponse updateCardInventoryReceive(CardInventory cardinventory);
	
	public List<CardReject> getAllCardRejects();
	public ObjResponse updateCardInventoryReject(CardReject cardreject);
	
	public ObjResponse updateCardStock(CardInventory cardinventory);

}
