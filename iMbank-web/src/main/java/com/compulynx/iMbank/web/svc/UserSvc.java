/**
 * 
 */
package com.compulynx.iMbank.web.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.UserBalImpl;
import com.compulynx.iMbank.models.LoginTypes;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Component
@Path("/user")
public class UserSvc {

	@Autowired
	UserBalImpl userBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUsers/{userLinkedId}")
	public Response GetUsers(@PathParam("userLinkedId") int userLinkedId) {
		try {
			List<User> users = userBal.GetAllUsers(userLinkedId);
			if (!(users == null)) {
				return Response.status(200).entity(users).build();
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
	@Path("/gtUsers")
	public Response getUsers() {
		try {
			List<User> users = userBal.GetAllUsers();
			if (!(users == null)) {
				return Response.status(200).entity(users).build();
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
	@Path("/gtClasses/")
	public Response GetClasses() {
		try {
			List<User> classes = userBal.GetClasses();
			if (!(classes == null)) {
				return Response.status(200).entity(classes).build();
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
	@Path("/gtQuestions/")
	public Response GetQuestions() {
		try {
			List<User> questions = userBal.GetQuestions();
			if (!(questions == null)) {
				return Response.status(200).entity(questions).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updUser")
	public Response UpdateUser(User user) {

		ObjResponse response = userBal.UpdateUser(user);
		return Response.status(response.respCode).entity(response).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUserById/{userId}")
	public Response GetUserById(@PathParam("userId") int userId) {
		try {
			User user = userBal.GetUserById(userId);
			if (!(user == null)) {
				return Response.status(200).entity(user).build();
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
	@Path("/getlogintypes")
	public Response getLoginTypes() {
		try {
			List<LoginTypes> detail = userBal.getLoginTypesList();
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
	@Path("/getAttempts")
	public Response getAttempts() {

		List<User> users = userBal.getUserAttempts();
		return Response.status(200).entity(users).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updUserAttempts")
	public Response UpdateUserAttempts(User user) {

		ObjResponse response = userBal.resetFailAttempts(user);
		return Response.status(response.respCode).entity(response).build();

	}


	/**
	 *
	 * @return
     */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/unlock")
	public Response unlockUser() {
		User user=new User();
		user.userName = "admin";
		user.isLocked = false;
		ObjResponse response= userBal.unlockUser(user);
		return Response.status(200).entity(response).build();
	}


}
