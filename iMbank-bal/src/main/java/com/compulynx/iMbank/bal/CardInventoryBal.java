package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CardReject;
import com.compulynx.iMbank.models.ObjResponse;


public interface CardInventoryBal {
	public ObjResponse createUpdateCardInventory(CardInventory cardinventory);
	public List<CardInventory> getAllCardInventory();
	public ObjResponse updateCardInventoryReceive(CardInventory cardinventory);
	
	public List<CardReject> getAllCardRejects();
	public ObjResponse updateCardInventoryReject(CardReject cardreject);
	
	public ObjResponse updateCardStock(CardInventory cardinventory);
}
