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

import com.compulynx.iMbank.bal.impl.BranchBalImpl;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Printer;
import com.compulynx.iMbank.models.Regions;

@Component
@Path("/branch")
public class BranchSvc {

	@Autowired
	BranchBalImpl branchBal;
	Logger logger = Logger.getLogger(BranchSvc.class.getCanonicalName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtprinters")
	public Response getPrinterListings() {
		//logger.info("Get Printer Listing Data");
		try {
			List<Printer> detail = branchBal.getAllPrinters();
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
	 * create update Product
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdateProduct")
	public Response createUpdateProduct(Printer printer) {
		logger.info("Product>>" + printer.serialNumber);

		ObjResponse response = branchBal.createUpdatePrinter(printer);
		return Response.status(response.respCode).entity(response).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createUpdatebranch")
	public Response createUpdateBranch(Branch branch) {
		logger.info("Branch>>" + branch.branchId);
		ObjResponse response = branchBal.createUpdateBranch(branch);
		return Response.status(response.respCode).entity(response).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtbranch")
	public Response getBranchs() {
		//logger.info("Get Branch  Data");
		List<Branch> detail = branchBal.getAllBranchs();
		//System.out.println("branch detail" + detail);
		if (!(detail == null)) {
			return Response.status(200).entity(detail).build();
		} else {
			return Response.status(201).entity(null).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtregions")
	public Response getRegions() {
		logger.info("Get Region Data");
		List<Regions> regiondetails = branchBal.getAllRegions();
		//System.out.println("region detail" + regiondetails);
		if (!(regiondetails == null)) {
			return Response.status(200).entity(regiondetails).build();
		} else {
			return Response.status(201).entity(null).build();
		}
	}
}
