package com.compulynx.iMbank.web.svc;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.CardPrintBalImp;
import com.compulynx.iMbank.bal.impl.CurrentBalanceBalImpl;
import com.compulynx.iMbank.models.CardPrint;
import com.compulynx.iMbank.models.ObjResponse;

@Component
@Path("/cardprint")
public class CardPrintSvc {
	Logger logger = Logger.getLogger(CardPrintSvc.class.getCanonicalName());
	@Autowired
	CardPrintBalImp cardprintBal;

	@Autowired
	CurrentBalanceBalImpl currentbalanceBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtaccdtls/{userId}")
	public Response getAccDetails(@PathParam("userId") String userId) {
		logger.info("Get Acc Data");
		List<CardPrint> accountdetails = cardprintBal.getAccDetails(userId);
		if (!(accountdetails == null)) {
			return Response.status(200).entity(accountdetails).build();
		} else {
			return Response.status(201).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdateCard")
	public Response createUpdateProduct(CardPrint cardPrint) {
		logger.info("CardPrint>>\t"+new Date()+"\t" + cardPrint.accountName);

		ObjResponse response = cardprintBal.createUpdateCard(cardPrint);
		/*
		 * CurrentBalance currentbalance=new CurrentBalance();
		 * currentbalance.branchId = cardPrint.branchPrinted; ObjResponse res =
		 * currentbalanceBal.updateCurrentBalance(currentbalance);
		 */
		return Response.status(response.respCode).entity(response).build();

	}
}
