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
import com.compulynx.iMbank.bal.impl.EmailConfigBalImpl;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.EmailConfig;
import com.compulynx.iMbank.models.ObjResponse;

@Component
@Path("/config")
public class EmailConfigSvc {
	@Autowired
	EmailConfigBalImpl emailconfigBal;
	Logger logger = Logger.getLogger(EmailConfigSvc.class.getCanonicalName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtConfig")
	public Response getConfig() {
		logger.info("Get Config");
		try {
			List<EmailConfig> detail = emailconfigBal.getAllEmailConfig();
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
	 * create update Config
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdateConfig")
	public Response createUpdateConfig(EmailConfig emailconfig) {

		ObjResponse response = emailconfigBal
				.createUpdateEmailConfig(emailconfig);
		return Response.status(response.respCode).entity(response).build();

	}

}
