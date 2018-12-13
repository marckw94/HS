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

import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation;
import org.marcoWenzel.middleware.highSchool.model.Notificatio;
import org.marcoWenzel.middleware.highSchool.model.Notification_Id;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.model.Teacher;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.NotificationWrapper;

@Secured({Category.Admin})
@Path("Admin/Notif")
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class NotificationResource {
 NotificationDAO notificationDao = new NotificationDAO();
 ParentDAO parentDao = new ParentDAO();
 TeacherDAO teacherDao = new TeacherDAO(); 
 EvaluationDAO courseClassDao = new EvaluationDAO();
 StudentDAO studentDao = new StudentDAO();
 CCAssotiationDAO ccaDao = new CCAssotiationDAO();
 CourseDAO courseDao = new CourseDAO();
 Calendar cal = Calendar.getInstance();
 SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
 
 @POST
 @Path("newParentNotif/{parent_id}")
 public Response createParentNotif(@PathParam("parent_id")String id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (newN.getReceiver() == null)
		 throw new DataNotFoundException();
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(cal.getTime());
	 notification.getPrimaryKey().setNotificationNumber(maxid);
	 if (newN.getReceiver().equals(id))
		 notification.getPrimaryKey().setReceiver(newN.getReceiver());
	 else
		 throw new DataNotFoundException();
	 
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("allParents").build().toString();
	 addLinkToList(uris, uri, "send to all parent", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
     addLinkToList(uris, uri, "general services", "GET");
	 
     GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 if(newN != null &&  notificationDao.create(notification)){
		 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
	 }else
		 throw new DataNotFoundException();
	 }
 
 @POST
 @Path("newTeacherNotif/{teacher_id}")
 public Response createTeacherNotif(@PathParam("teacher_id")String id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (newN.getReceiver() == null)
		 throw new DataNotFoundException();
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(cal.getTime());
	 notification.getPrimaryKey().setNotificationNumber(maxid);
	 notification.getPrimaryKey().setReceiver(newN.getReceiver());
	 if (newN.getReceiver().equals(id))
		 notification.getPrimaryKey().setReceiver(newN.getReceiver());
	 else
		 throw new DataNotFoundException();
	 
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("allTeachers").build().toString();
	 addLinkToList(uris, uri, "send to all teacher", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
     addLinkToList(uris, uri, "general services", "GET");
	 
     GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 if(newN != null &&  notificationDao.create(notification)){
		 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
	 }else
		 throw new DataNotFoundException();
	 }
 
 @Path("allTeachers")
 @POST
 public Response createAllTeacher(NotificationWrapper newN,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (!newN.getReceiver().equals("teachers"))
		 throw new DataNotFoundException();
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(cal.getTime());
	 
	 int idNote=notificationDao.maxid("primaryKey.notificationNumber");
	
	 List<Teacher> teacher= teacherDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
     
	 if (teacher == null ) {
    	 
    	 throw new DataNotFoundException();
    	 }
     
     for (Teacher t:teacher) {
    	 notification.getPrimaryKey().setReceiver(t.getTeacherId());
    	 notification.getPrimaryKey().setNotificationNumber(idNote);
	     if ( !notificationDao.create(notification)) 
	    	 throw new DataNotFoundException();
	     idNote++;
	     
	     uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newTeacherNotif")
	    		 .path(t.getTeacherId()).build().toString();
	     addLinkToList(uris, uri, "send specific notification", "POST");
	    
	 }
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

     return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
 }
 
 @Path("allParents")
 @POST
 public Response createAllPar(NotificationWrapper newN,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (!newN.getReceiver().equals("parents"))
		 throw new DataNotFoundException();
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(cal.getTime());
	 int idNote=notificationDao.maxid("primaryKey.notificationNumber");
	 List<Parent> parent= parentDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
     
	 if (parent == null ) {
    	 
    	 throw new DataNotFoundException();     }
     
     for (Parent p:parent) {
    	 notification.getPrimaryKey().setReceiver(p.getUsername());
    	 notification.getPrimaryKey().setNotificationNumber(idNote);
	     if ( !notificationDao.create(notification)) 
	    	 throw new DataNotFoundException();
	     idNote++;
	     uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newParentNotif")
	    		 .path(p.getUsername()).build().toString();
	     addLinkToList(uris, uri, "send specific notification", "POST");
	     
	 }
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

     return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
 }
 
 @Path("ClassParents/{class_id}")
 @POST
 public Response createParClass(@PathParam("class_id") int id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (!newN.getReceiver().equals("class"))
		 throw new DataNotFoundException();
	 List<Student> allStudent = studentDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassTeachers").path(Long.toString(id))
			 .build().toString();
	 addLinkToList(uris, uri, "send notification to interested  teachers", "POST");
	 
	 for (Student s: allStudent) {
		 if (s.getEnrolledClass()!=null && s.getEnrolledClass().getIdClass()==id) {
			 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
			 Notificatio notification = new Notificatio();
			 notification.setPrimaryKey(new Notification_Id());
			 notification.setContent(newN.getContent());
			 notification.setContentType(newN.getContentType());
			 notification.setSendDate(cal.getTime());
			 notification.getPrimaryKey().setNotificationNumber(maxid);
			 notification.getPrimaryKey().setReceiver(s.getParentUsername().getUsername());
			 
			 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newParentNotif")
		    		 .path(s.getParentUsername().getUsername()).build().toString();
		     addLinkToList(uris, uri, "send specific notification", "POST");
		     
		     if ( !notificationDao.create(notification)) {
		    	 throw new DataNotFoundException();		     }
		 }
	 }
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
	
 }
 
 
 @Path("ClassTeachers/{class_id}")
 @POST
 public Response createTeachClass(@PathParam("class_id") int id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	 if (!newN.getReceiver().equals("class"))
		 throw new DataNotFoundException();
	 List<CourseClassAssociation>ccaList= ccaDao.findAll();
	 List<Course>coursePerClass= new ArrayList<Course>();
	 for(CourseClassAssociation singleCCA: ccaList) {
		 if(singleCCA.getPrimaryKey().getClass_id()==id) {
			 try {
				 coursePerClass.add(courseDao.get(singleCCA.getPrimaryKey().getCourse_id()));
			 }catch (Exception e) {
				 throw new DataNotFoundException();			}
		 }
	 }
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 for(Course c : coursePerClass) {
		 Notificatio notification = new Notificatio();
		 notification.setPrimaryKey(new Notification_Id());
		 notification.setContent(newN.getContent());
		 notification.setContentType(newN.getContentType());
		 notification.setSendDate(cal.getTime());
		
		 notification.getPrimaryKey().setNotificationNumber(maxid);
		 notification.getPrimaryKey().setReceiver(c.getTeacher().getTeacherId());
		 
		 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newTeacherNotif").path(c.getTeacher().getTeacherId())
				 .build().toString();
		 addLinkToList(uris, uri, "send to specific teacher", "POST");
		 if(!notificationDao.create(notification)) {
			  throw new DataNotFoundException();
		}
	 }
	 
	 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassParents").path(Long.toString(id))
			 .build().toString();
	 addLinkToList(uris, uri, "send to interested parents", "POST");
	 
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
  	 
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
  	 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
     
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
