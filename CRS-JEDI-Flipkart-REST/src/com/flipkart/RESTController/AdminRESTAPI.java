package com.flipkart.RESTController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.flipkart.dao.AdminDAOOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flipkart.bean.Course;
import com.flipkart.service.AdminOperation;


@Path("/admin")
public class AdminRESTAPI {
	
	
	@GET
	@Path("/getReportCard")
	@Produces(MediaType.APPLICATION_JSON)
	public void getReportCard(@QueryParam("id") Integer id)
	{
		AdminDAOOperation adminDAO = AdminDAOOperation.getInstance();
		adminDAO.printGrades(id);
	}

	AdminOperation adminOperation = AdminOperation.getInstance();

	@PUT
	@Path("/openRegistration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response openRegistration() {

		adminOperation.startRegistrationWindow();
		return Response.status(201).entity("Window Opened").build();

	}

	@PUT
	@Path("/closeRegistration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response closeRegistration() {
		
		adminOperation.closeRegistrationWindow();
		return Response.status(201).entity("Window Closed").build();
	}
	

	@POST
	@Path("/addCourse")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCourse(Course course) {
		adminOperation.addCourse2(course);
		return Response.status(201).entity("Course Added").build();
	}
}
