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

import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
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
@Path("Notif")
public class NotificationResource {
 NotificationDAO notificationDao = new NotificationDAO();
 ParentDAO parentDao = new ParentDAO();
 TeacherDAO teacherDao = new TeacherDAO(); 
 EvaluationDAO courseClassDao = new EvaluationDAO();
 
 @POST
 @Path("newParentNotif/{parent_id}")
 @Produces(MediaType.APPLICATION_XML)
 @Consumes(MediaType.APPLICATION_XML)
 public Response createParentNotif(@PathParam("parent_id")String id,NotificationWrapper newN
		 ,@Context UriInfo uriInfo) {
	 if (newN.getUserName() == null)
		 return Response.status(Response.Status.BAD_REQUEST).build();
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 notification.getPrimaryKey().setNotificationNumber(newN.getNotificationNumber());
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
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 notification.getPrimaryKey().setNotificationNumber(newN.getNotificationNumber());
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
	 int idNote = notificationDao.maxNotifid();
	
	 
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
	 int idNote = notificationDao.maxNotifid();
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
     List<Evaluation> classes= courseClassDao.findAll();
     Set<String> parentStudentInClass = new HashSet<String>();
     Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
	 List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassTeach").path(Long.toString(id))
			 .build().toString();
	 addLinkToList(uris, uri, "send notification to the teacher", "POST");
     if (classes == null ) {
         return Response.status(Response.Status.BAD_REQUEST).build();
     }
     for(Evaluation cc :classes) {
    	 if(cc.getId().getCourseId().getIdCourse()==id) {
    		 parentStudentInClass.add(cc.getId().getStudentId().getParentUsername().getUsername());
    	 }
     }
     Iterator<String> onParent = parentStudentInClass.iterator();
     int maxid=notificationDao.maxNotifid();
     while(onParent.hasNext()) {
    	 String gg =(String) onParent.next();
		 System.out.println(gg);
		 notification.getPrimaryKey().setNotificationNumber(maxid);
		 //DA RISOLVERE: non funziiona se abbiamo due insegnanti con lo stesso cognome
		 notification.getPrimaryKey().setUserName(gg);
		 maxid++;
		 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("newParentNotif")
	    		 .path(gg).build().toString();
	     addLinkToList(uris, uri, "send specific notification", "POST");
	     if ( !notificationDao.create(notification)) {
	    	 return Response.status(Response.Status.BAD_REQUEST).build();
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
	
	 Notificatio notification = new Notificatio();
	 notification.setPrimaryKey(new Notification_Id());
	 notification.setContent(newN.getContent());
	 notification.setContentType(newN.getContentType());
	 notification.setSendDate(newN.getSendDate());
     List<Evaluation> classes= courseClassDao.findAll();
     Teacher teacher = null;
     List<Link> uris = new ArrayList<Link>();
	 String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	 addLinkToList(uris, uri, "self", "POST");
	 uri= uriInfo.getBaseUriBuilder().path(NotificationResource.class).path("ClassPar").path(Long.toString(id))
			 .build().toString();
	 addLinkToList(uris, uri, "send to interested parent", "POST");
     if (classes == null ) {
         return Response.status(Response.Status.BAD_REQUEST).build();
     }
     for(Evaluation cc :classes) {
    	 if(cc.getId().getCourseId().getIdCourse()==id) {
    		 teacher = cc.getId().getCourseId().getTeacher();
    	 }
     }
     int maxid=notificationDao.maxNotifid(); 
     notification.getPrimaryKey().setNotificationNumber(maxid);
     notification.getPrimaryKey().setUserName(teacher.getSurname());
     uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
     addLinkToList(uris, uri, "general services", "GET");
     GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	 //DA RISOLVERE: non funziiona se abbiamo due insegnanti con lo stesso cognome
     if ( notificationDao.create(notification)) {
	    return Response.status(Response.Status.OK).entity(e).build();
 	}else {
 		return Response.status(Response.Status.BAD_REQUEST).build();
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
