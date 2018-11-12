package org.marcoWenzel.middleware.highSchool.resources;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Classes;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation;
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
	CCAssotiationDAO ccaDao = new CCAssotiationDAO();
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	
	
	@POST
    @Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
    public Response newAppointment(@PathParam("user_id")String userId,AppointmentWrapper aw,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		String parent=aw.getParentUsername();
		String teacher=aw.getTeacherId();
		Parent parentObj=parentDao.get(parent);
		System.out.println("parnetUser: "+parent);
		System.out.println("TeachrUser: "+teacher);
		System.out.println("Path: "+userId);
		Teacher teacherObj= teacherDao.get(teacher);
		int validityCheck=0;
		if (parentObj==null || teacherObj==null)
			 throw new DataNotFoundException();
		List<Classes>enrolledSonClass= new ArrayList<Classes>();
		for(Student s:parentObj.getSon()) {
			enrolledSonClass.add(s.getEnrolledClass());
		}
		System.out.println("classes son: "+ enrolledSonClass);
		List<Course> courseKeepTeacher= new ArrayList<Course>();
		List<CourseClassAssociation> ccaList = ccaDao.findAll();
		List<Integer> courseFollowSons= new ArrayList<Integer>();
		
		for(CourseClassAssociation cca: ccaList) {
			int classId=cca.getPrimaryKey().getClass_id();
			for(Classes e : enrolledSonClass) {
				if(classId==e.getIdClass())
					 courseFollowSons.add(classId);
			}	
		}
		System.out.println("course of sons: "+ courseFollowSons);
		for(Course c :teacherObj.getCourseKeep()) {
			if (courseFollowSons.contains(c.getIdCourse()))
				validityCheck=1;
		}
		if (validityCheck==0)
			 throw new DataNotFoundException();				
		//cosi non funziona ma per testare per ora va bene,poiaggiungere i vari param...
		Appointment newApp = new Appointment();
		int maxid=appointmentDao.maxid("appointId");
		newApp.setAppointId(maxid);
		newApp.setParentId(parent);
		newApp.setTeacherId(teacher);
		if (aw.getAppointmentDate().before(cal.getTime()))
			 throw new DataNotFoundException();
		newApp.setAppointmentDate(aw.getAppointmentDate());
		
	
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
			 throw new DataNotFoundException();		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (appointmentDao.create(newApp)) {
	            return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
        }
        else {
        	 throw new DataNotFoundException();
        	 }
        
    }
	public void addLinkToList(List<Link> list,String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		list.add(newL);
	}
	public String negotiation(HttpHeaders h) {
		String accept=h.getHeaderString(HttpHeaders.CONTENT_TYPE);
		return accept;
	}
}
