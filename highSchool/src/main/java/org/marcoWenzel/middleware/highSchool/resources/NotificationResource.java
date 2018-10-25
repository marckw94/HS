package org.marcoWenzel.middleware.highSchool.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation;
import org.marcoWenzel.middleware.highSchool.model.Evaluation;
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
public class NotificationResource {
 NotificationDAO notificationDao = new NotificationDAO();
 ParentDAO parentDao = new ParentDAO();
 TeacherDAO teacherDao = new TeacherDAO(); 
 EvaluationDAO courseClassDao = new EvaluationDAO();
 StudentDAO studentDao = new StudentDAO();
 CCAssotiationDAO ccaDao = new CCAssotiationDAO();
 CourseDAO courseDao = new CourseDAO();
 @POST
 @Path("newParentNotif/{parent_id}")
 @Produces(MediaType.APPLICATION_XML)
 @Consumes(MediaType.APPLICATION_XML)
 public Response createParentNotif(@PathParam("parent_id")String id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo) {
	 if (newN.getUserName() == null)
		 return Response.status(Response.Status.BAD_REQUEST).build();
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 notification.getPrimaryKey().setNotificationNumber(maxid);
	 notification.getPrimaryKey().setUserName(newN.getUserName());
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("allPar").build().toString();
	 addLinkToList(uris, uri, "send to all parent", "POST");
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
     addLinkToList(uris, uri, "general services", "GET");
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 if(newN != null &&  notificationDao.create(notification)){
		 return Response.status(Response.Status.OK).entity(e).build();
	 }else
		 return Response.status(Response.Status.BAD_REQUEST).build();
 }
 @POST
 @Path("newTeacherNotif/{teacher_id}")
 @Produces(MediaType.APPLICATION_XML)
 @Consumes(MediaType.APPLICATION_XML)
 public Response createTeacherNotif(@PathParam("teacher_id")String id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo) {
	 if (newN.getUserName() == null)
		 return Response.status(Response.Status.BAD_REQUEST).build();
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 notification.getPrimaryKey().setNotificationNumber(maxid);
	 notification.getPrimaryKey().setUserName(newN.getUserName());
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("allTeach").build().toString();
	 addLinkToList(uris, uri, "send to all teacher", "POST");
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
     addLinkToList(uris, uri, "general services", "GET");
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 if(newN != null &&  notificationDao.create(notification)){
		 return Response.status(Response.Status.OK).entity(e).build();
	 }else
		 return Response.status(Response.Status.BAD_REQUEST).build();
 }
 
 @Path("allTeach")
 @POST
 @Consumes(MediaType.APPLICATION_XML)
 @Produces(MediaType.APPLICATION_XML)
 public Response createAllTeacher(NotificationWrapper newN,@Context UriInfo uriInfo) {
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	System.out.println("ok");
	 int idNote=notificationDao.maxid("primaryKey.notificationNumber");
	
	 
	 List<Teacher> teacher= teacherDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
     if (teacher == null ) {
    	 
         return Response.status(Response.Status.BAD_REQUEST).build();
     }
     
     for (Teacher t:teacher) {
    	 notification.getPrimaryKey().setUserName(t.getSurname());
    	 notification.getPrimaryKey().setNotificationNumber(idNote);
	     if ( !notificationDao.create(notification)) 
	    	 return Response.status(Response.Status.BAD_REQUEST).build();
	     idNote++;
	     uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newTeacherNotif")
	    		 .path(t.getTeacherId()).build().toString();
	     addLinkToList(uris, uri, "send specific notification", "POST");
	     uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.build().toString();
	     addLinkToList(uris, uri, "general services", "GET");
	 }
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

     return Response.status(Response.Status.OK).entity(e).build();
 }
 
 @Path("allPar")
 @POST
 @Consumes(MediaType.APPLICATION_XML)
 @Produces(MediaType.APPLICATION_XML)
 public Response createAllPar(NotificationWrapper newN,@Context UriInfo uriInfo) {
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 int idNote=notificationDao.maxid("primaryKey.notificationNumber");
	 List<Parent> parent= parentDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
     if (parent == null ) {
    	 
         return Response.status(Response.Status.BAD_REQUEST).build();
     }
     
     for (Parent p:parent) {
    	 notification.getPrimaryKey().setUserName(p.getUsername());
    	 notification.getPrimaryKey().setNotificationNumber(idNote);
	     if ( !notificationDao.create(notification)) 
	    	 return Response.status(Response.Status.BAD_REQUEST).build();
	     idNote++;
	     uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newParentNotif")
	    		 .path(p.getUsername()).build().toString();
	     addLinkToList(uris, uri, "send specific notification", "POST");
	     uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.build().toString();
	     addLinkToList(uris, uri, "general services", "GET");
	 }
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

     return Response.status(Response.Status.OK).entity(e).build();
 }
 @Path("ClassPar/{class_id}")
 @POST
 @Consumes(MediaType.APPLICATION_XML)
 @Produces(MediaType.APPLICATION_XML)
 public Response createParClass(@PathParam("class_id") int id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo) {
	 List<Student> allStudent = studentDao.findAll();
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 if (newN==null) {
		 return Response.status(Response.Status.BAD_REQUEST).build();
	 }
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 for (Student s: allStudent) {
		 if (s.getEnrolledClass().getIdClass()==id) {
			 
			 System.out.println("maxid: "+ maxid);
			 Notificatio notification = new Notificatio();
			 notification.setPrimaryKey(new Notification_Id());
			 notification.setContent(newN.getContent());
			 notification.setContentType(newN.getContentType());
			 notification.setSendDate(newN.getSendDate());
			 notification.getPrimaryKey().setNotificationNumber(maxid);
			 notification.getPrimaryKey().setUserName(s.getParentUsername().getUsername());
			 
			 addLinkToList(uris, uri, "self", "POST");
			 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassTeach").path(Long.toString(id))
					 .build().toString();
			 addLinkToList(uris, uri, "send notification to the teacher", "POST");
			 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newParentNotif")
		    		 .path(s.getParentUsername().getUsername()).build().toString();
		     addLinkToList(uris, uri, "send specific notification", "POST");
		     
		     if ( !notificationDao.create(notification)) {
		    	 return Response.status(Response.Status.BAD_REQUEST).build();
		     }
		 }
	 }
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 return Response.status(Response.Status.OK).entity(e).build();
	
 }
 
 
 @Path("ClassTeach/{class_id}")
 @POST
 @Consumes(MediaType.APPLICATION_XML)
 @Produces(MediaType.APPLICATION_XML)
 public Response createTeachClass(@PathParam("class_id") int id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo) {
	 List<CourseClassAssociation>ccaList= ccaDao.findAll();
	 List<Course>coursePerClass= new ArrayList<Course>();
	 for(CourseClassAssociation singleCCA: ccaList) {
		 if(singleCCA.getPrimaryKey().getClass_id()==id) {
			 try {
				 coursePerClass.add(courseDao.get(singleCCA.getPrimaryKey().getCourse_id()));
			 }catch (Exception e) {
				 return Response.status(Response.Status.BAD_REQUEST).build();
			}
		 }
	 }
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 int maxid=notificationDao.maxid("primaryKey.notificationNumber");
	 for(Course c : coursePerClass) {
		 Notificatio notification = new Notificatio();
		 notification.setPrimaryKey(new Notification_Id());
		 notification.setContent(newN.getContent());
		 notification.setContentType(newN.getContentType());
		 notification.setSendDate(newN.getSendDate());
		 addLinkToList(uris, uri, "self", "POST");
		 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassPar").path(Long.toString(id))
				 .build().toString();
		 addLinkToList(uris, uri, "send to interested parent", "POST");
		  notification.getPrimaryKey().setNotificationNumber(maxid);
		  notification.getPrimaryKey().setUserName(c.getTeacher().getTeacherId());
		  
		  if(!notificationDao.create(notification)) {
			  return Response.status(Response.Status.BAD_REQUEST).build();
		  }
	 }
	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
	 addLinkToList(uris, uri, "general services", "GET");
  	 GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
  	 return Response.status(Response.Status.OK).entity(e).build();
     
 }
 
	public void addLinkToList(List<Link> list,String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		list.add(newL);
	}
}
