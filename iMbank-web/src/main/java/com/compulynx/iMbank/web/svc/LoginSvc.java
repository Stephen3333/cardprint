package com.compulynx.iMbank.web.svc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.LoginBalImpl;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.LoginSession;
import com.compulynx.iMbank.models.LoginUser;
import com.compulynx.iMbank.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Component
@Path("/login")
public class LoginSvc {

	// Logger logger=Logger.getLogger(LoginSvc.class.getName());
	Logger logger = Logger.getLogger(LoginSvc.class);

	@Autowired
	LoginBalImpl loginBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/testlogin")
	public Response testLogin() {

		return Response.status(200).entity("Login Service is running!!")
				.build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/manualAuth")
	public Response ValidateManualLogin(User user) {
		logger.info("Entering Auth");
		// logger.error("Not an error really");
		LoginUser loginUser = loginBal.ValidateManualLogin(user.userName,
				user.userPwd);
		return Response.status(200).entity(loginUser).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/forgotpwd")
	public Response ForgotPassword(String userName) {
		LoginUser loginUser = loginBal.ForgotPassword(userName);
		return Response.status(200).entity(loginUser).build();

	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUserRights/{usrId}")
	public Response GetUserLoginSession(@PathParam("usrId") int usrId) {
		try {
			LoginSession loginSession = loginBal.GetLoginSession(usrId);
			return Response.status(loginSession.respCode).entity(loginSession)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	// //get branchlist
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUserBranches/{usrId}")
	public Response GetUserBranches(@PathParam("usrId") int usrId) {
		try {
			List<Branch> branches = loginBal.GetUserBranches(usrId);
			if (!(branches == null)) {
				return Response.status(200).entity(branches).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
		/*
		 * try { LoginSession loginSession = loginBal.GetUserBranches(usrId);
		 * return
		 * Response.status(loginSession.respCode).entity(loginSession).build();
		 * } catch (Exception ex) { ex.printStackTrace(); return
		 * Response.status(404).entity(null).build(); }
		 */
	}

	// Password Reset
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/resetPassword")
	public Response resetPassword(User user) {
		LoginUser loginUser = loginBal.resetPassword(user);
		return Response.status(200).entity(loginUser).build();

	}
}
