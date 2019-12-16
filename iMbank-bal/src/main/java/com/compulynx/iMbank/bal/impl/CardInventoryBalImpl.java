package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.CardInventoryBal;
import com.compulynx.iMbank.dal.impl.CardInventoryDalImpl;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CardReject;
import com.compulynx.iMbank.models.ObjResponse;

public class CardInventoryBalImpl implements CardInventoryBal{
	@Autowired
    CardInventoryDalImpl cardinventoryDal;

	@Override
	public ObjResponse createUpdateCardInventory(CardInventory cardinventory) {
		return cardinventoryDal.createUpdateCardInventory(cardinventory);
	}

	@Override
	public List<CardInventory> getAllCardInventory() {
		return cardinventoryDal.getAllCardInventory();
	}

	@Override
	public ObjResponse updateCardInventoryReceive(CardInventory cardinventory) {
		// TODO Auto-generated method stub
		return cardinventoryDal.updateCardInventoryReceive(cardinventory);
	}

	public List<CardReject> getAllCardRejects() {
		// TODO Auto-generated method stub
		return cardinventoryDal.getAllCardRejects();
	}

	public ObjResponse updateCardInventoryReject(CardReject cardreject) {
		// TODO Auto-generated method stub
		return cardinventoryDal.updateCardInventoryReject(cardreject);
	}

	public ObjResponse updateCardStock(CardInventory cardinventory) {
		// TODO Auto-generated method stub
		return cardinventoryDal.updateCardStock(cardinventory);
	}

}
