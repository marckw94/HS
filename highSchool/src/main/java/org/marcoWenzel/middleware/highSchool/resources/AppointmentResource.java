package org.marcoWenzel.middleware.highSchool.resources;


import java.util.ArrayList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Appointment_Id;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.model.Teacher;
import org.marcoWenzel.middleware.highSchool.response.AppointmentResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.AppointmentWrapper;
@Secured({Category.Parent,Category.Teacher})
@Path("Appoint/{user_id}")
public class AppointmentResource {
	AppointmentDAO appointmentDao = new AppointmentDAO();
	TeacherDAO teacherDao = new TeacherDAO();
	ParentDAO parentDao = new ParentDAO();
	@POST
    @Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
    public Response newAppointment(@PathParam("user_id")String userId,AppointmentWrapper aw,
    		@Context UriInfo uriInfo) {
		//cosi non funziona ma per testare per ora va bene,poiaggiungere i vari param...
		Appointment newApp = new Appointment();
		Teacher teacher = teacherDao.get(aw.getTeacherId());
		System.out.println(teacher.getSurname());
		Parent parent = parentDao.get(aw.getParentUsername());
		newApp.setId(new Appointment_Id());
		newApp.getId().setTeacher(teacher);
		newApp.getId().setParents(parent);
		newApp.getId().setAppointmentDate(aw.getAppointmentDate());
		if (teacher==null || parent==null)
			return Response.status(Response.Status.BAD_REQUEST).entity("ciao").build();
	
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		if(aw.getTeacherId().equals(userId)) {
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
			.resolveTemplate("user_id", userId).path("appointments").build().toString();
			addLinkToList(uris, uri, "see appointments", "GET");
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id",userId).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
		}else if (aw.getParentUsername().equals(userId))  {
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id", userId).path("appointments").build().toString();
			addLinkToList(uris, uri, "see appointments", "GET");
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id",userId).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
		}else {
			return Response.status(Response.Status.BAD_REQUEST).entity("ddd").build();
		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (appointmentDao.create(newApp)) {
	            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("eee").build();
        }
        
    }
	public void addLinkToList(List<Link> list,String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		list.add(newL);
	}
}
