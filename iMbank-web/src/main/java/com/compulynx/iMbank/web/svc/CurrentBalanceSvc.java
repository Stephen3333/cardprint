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
import com.compulynx.iMbank.bal.impl.CurrentBalanceBalImpl;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CurrentBalance;
import com.compulynx.iMbank.models.ObjResponse;

@Component
@Path("/currentbalance")
public class CurrentBalanceSvc {
	@Autowired
	CurrentBalanceBalImpl currentbalanceBal;
	Logger logger = Logger
			.getLogger(CurrentBalanceSvc.class.getCanonicalName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCurrentBalance")
	public Response getCurrentBalance() {
		logger.info("Get CardBalance Listing Data");
		try {
			List<CurrentBalance> detail = currentbalanceBal
					.getAllCurrentBalance();
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
	@Path("/updateCurrentBalance")
	public Response updateCurrentBalance(CurrentBalance currentbalance) {

		ObjResponse response = currentbalanceBal
				.updateCurrentBalance(currentbalance);
		return Response.status(response.respCode).entity(response).build();

	}

}
