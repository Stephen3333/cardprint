package com.compulynx.iMbank.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.iMbank.bal.impl.UserGroupBalImpl;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.UserGroup;

@Component
@Path("/userGroups")
public class UserGroupSvc {

	@Autowired
	UserGroupBalImpl userGroupBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroups/{linkId}")
	public Response GetGroups(@PathParam("linkId") int linkId) {
		try {
			List<UserGroup> groups = userGroupBal.GetAllGroups(linkId);
			if (!(groups == null)) {
				return Response.status(200).entity(groups).build();
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
	@Path("/gtRights/")
	public Response GetRights() {
		try {
			List<RightsDetail> rights = userGroupBal.GetRights();
			if (!(rights == null)) {
				return Response.status(200).entity(rights).build();
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
	@Path("/gtAllRights/")
	public Response GetAllRights() {
		try {
			List<RightsDetail> rights = userGroupBal.GetAllRights();
			if (!(rights == null)) {
				return Response.status(200).entity(rights).build();
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
	@Path("/gtHeaders/")
	public Response GetHeaders() {
		try {
			List<Rights> rights = userGroupBal.getHeaders();
			if (!(rights == null)) {
				return Response.status(200).entity(rights).build();
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
	@Path("/updGroup")
	public Response UpdateGroup(UserGroup group) {
		ObjResponse response = userGroupBal.UpdateUserGroup(group);
		return Response.status(response.respCode).entity(response).build();

	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updRights")
	public Response UpdateRights(RightsDetail right) {
		ObjResponse response = userGroupBal.UpdateRight(right);
		return Response.status(response.respCode).entity(response).build();

	}
	/*
	 * get list of active groups
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroupsActive/{linkId}")
	public Response getActiveGroups(@PathParam("linkId") int linkId) {
		try {
			List<UserGroup> groups = userGroupBal.getActiveGroups(linkId);
			if (!(groups == null)) {
				return Response.status(200).entity(groups).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}
