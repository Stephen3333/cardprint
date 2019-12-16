package com.compulynx.iMbank.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.CardInventoryBalImpl;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CardReject;
import com.compulynx.iMbank.models.ObjResponse;

@Component
@Path("/cardinventory")
public class CardInventorySvc {
	@Autowired
	CardInventoryBalImpl cardinventoryBal;
	Logger logger = Logger.getLogger(CardInventorySvc.class.getCanonicalName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCardInventory")
	public Response getCardInventoryListings() {
		logger.info("Get Cardinventory Listing Data");
		try {
			List<CardInventory> detail = cardinventoryBal.getAllCardInventory();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(201).entity(null).build();
		}
	}

	/*
	 * create update CardInventory
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdateCardInventory")
	public Response createUpdateCardInventory(CardInventory cardinventory) {
		logger.info("CardInventory>>" + cardinventory.batchSize);

		ObjResponse response = cardinventoryBal
				.createUpdateCardInventory(cardinventory);
		return Response.status(response.respCode).entity(response).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateCardInventoryReceive")
	public Response updateCardInventoryReceive(CardInventory cardinventory) {

		ObjResponse response = cardinventoryBal
				.updateCardInventoryReceive(cardinventory);
		return Response.status(response.respCode).entity(response).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCardReject")
	public Response getCardRejects() {
		logger.info("Get Cards Reject Data");
		try {
			List<CardReject> detail = cardinventoryBal.getAllCardRejects();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(201).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateCardInventoryReject")
	public Response updateCardInventoryReject(CardReject cardreject) {

		ObjResponse response = cardinventoryBal
				.updateCardInventoryReject(cardreject);
		return Response.status(response.respCode).entity(response).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateCardStock")
	public Response updateCardStock(CardInventory cardinventory) {

		ObjResponse response = cardinventoryBal.updateCardStock(cardinventory);
		return Response.status(response.respCode).entity(response).build();

	}
}
