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

import com.compulynx.iMbank.bal.impl.CardTypeBalImpl;
import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.PrinterType;

@Component
@Path("/cardtype")
public class CardTypeSvc {
	@Autowired
	CardTypeBalImpl cardtypeBal;
	Logger logger = Logger.getLogger(CardTypeSvc.class.getCanonicalName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtcardtype")
	public Response getCardTypeListings() {
		//logger.info("Get Cardtype Listing Data");
		try {
			List<CardType> detail = cardtypeBal.getAllCardType();
			//System.out.println("card type detail" + detail);
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
	 * create update CardType
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdateCardType")
	public Response createUpdateCardType(CardType cardtype) {
		logger.info("Cardtype>>" + cardtype.name);

		ObjResponse response = cardtypeBal.createUpdateCardType(cardtype);
		return Response.status(response.respCode).entity(response).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtprintertype")
	public Response getPrinterTypes() {
		//logger.info("Get Printertype Listing Data");
		try {
			List<PrinterType> detail = cardtypeBal.getAllPrinterType();
			//System.out.println("printer type detail" + detail);
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
	@Path("/createUpdatePrinterType")
	public Response createUpdatePrinterType(PrinterType printertype) {
		logger.info("Printertype>>" + printertype.printerName);

		ObjResponse response = cardtypeBal.createUpdatePrinterType(printertype);
		return Response.status(response.respCode).entity(response).build();

	}

}
