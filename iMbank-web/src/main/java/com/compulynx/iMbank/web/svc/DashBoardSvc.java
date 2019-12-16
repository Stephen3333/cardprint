package com.compulynx.iMbank.web.svc;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.DashBoardBalImpl;
import com.compulynx.iMbank.models.DashBoard;

@Component
@Path("/dashBoard")
public class DashBoardSvc {
	@Autowired
	DashBoardBalImpl dashBoardBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCountDetail")
	public Response GetDashBoardCountDetail() {

		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardCountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtTransChartDetail")
	public Response GetTransChartDetail() {
		return null;
		/*
		 * try { List<DashBoard> detail = dashBoardBal.GetTransChartDetail(); if
		 * (!(detail == null)) { return
		 * Response.status(200).entity(detail).build(); } else { return
		 * Response.status(201).entity(null).build(); } } catch (Exception ex) {
		 * ex.printStackTrace(); return
		 * Response.status(404).entity(null).build(); }
		 */
	}

}
