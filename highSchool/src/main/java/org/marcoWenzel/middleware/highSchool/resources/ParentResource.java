package org.marcoWenzel.middleware.highSchool.resources;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.Course_ClassDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.PaymentDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Appointment_Id;
import org.marcoWenzel.middleware.highSchool.model.Course_Class;
import org.marcoWenzel.middleware.highSchool.model.Notificatio;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Payement;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.response.AppointmentResponse;
import org.marcoWenzel.middleware.highSchool.response.Course_ClassResponse;
import org.marcoWenzel.middleware.highSchool.response.NotificationResponse;
import org.marcoWenzel.middleware.highSchool.response.ParentResponse;
import org.marcoWenzel.middleware.highSchool.response.PaymentResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.AppointmentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Course_ClassWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.NotificationWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.ParentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.PaymentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Wrapper;
@Secured({Category.Parent})
@Path("parent/{user_id}")
public class ParentResource implements Principal{
	ParentDAO parentDao = new ParentDAO();
	StudentDAO studentDao = new StudentDAO();
	Course_ClassDAO course_classDao = new Course_ClassDAO();
	AppointmentDAO appointmentDao = new AppointmentDAO();
	NotificationDAO notificationDao = new NotificationDAO();
	PaymentDAO paymentDao = new PaymentDAO();
	@Context
	UriInfo uriInfo;
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getParentServices(@PathParam("user_id")String id,@Context UriInfo uriInfo) {
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("personal")
				.build().toString();
		addLinkToList(uris, uri, "see personal", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("appointments")
				.build().toString();
		addLinkToList(uris, uri, "see appointments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id)
				.build().toString();
		addLinkToList(uris, uri, "new appointment", "POST");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("History")
				.build().toString();
		addLinkToList(uris, uri, "see history payments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("Payments")
				.build().toString();
		addLinkToList(uris, uri, "see upcoming payments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("pubNotif")
				.build().toString();
		addLinkToList(uris, uri, "see public notifications", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("privNotif")
				.build().toString();
		addLinkToList(uris, uri, "see private notifications", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).build();
	}
	
	@Path("personal")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getPersonalData(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
        	
    		Parent thisParent = parentDao.get(id);
        	ParentResponse parentInterface = new ParentResponse();
        	parentInterface.setName(thisParent.getName());
        	parentInterface.setPassword(thisParent.getPassword());
        	parentInterface.setSurname(thisParent.getSurname());
        	parentInterface.setUsername(thisParent.getUsername());
        	String uri=uriInfo.getAbsolutePathBuilder().build().toString();
        	parentInterface.addLink(uri, "self","GET");
        	uri=uriInfo.getAbsolutePathBuilder().build().toString();
        	parentInterface.addLink(uri, "modify self","PUT");
        	uri = uriInfo.getBaseUriBuilder().path("parent").path(id).path("sons").build().toString();
        	parentInterface.addLink(uri, "sons","GET");
        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
    				.resolveTemplate("user_id",id).build().toString();
        	parentInterface.addLink(uri, "general services","GET");
        	if (thisParent.getUsername().equals(id)) {
	            return Response.status(Response.Status.OK).entity(parentInterface).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
        
    }
//triggerare nel caso di password	
	@PUT
	@Path("personal")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response setParentPersonal(ParentWrapper parIn,@Context UriInfo uriInfo) {
		
		Parent updateParent = parentDao.get(parIn.getUsername());
		updateParent.setName(parIn.getName());
		updateParent.setPassword(parIn.getPassword());
		updateParent.setSurname(parIn.getSurname());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "PUT");
		addLinkToList(uris, uri, "see self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",updateParent.getUsername()).build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (parentDao.update(updateParent)) {
	            return Response.status(Response.Status.OK).entity(e).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	}
	
	@GET
	@Path("sons")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response  getSonsList(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		List<StudentResponse> studentInterfaces = new ArrayList<StudentResponse>();
    	Parent thisParent = parentDao.get(id);
    	Iterator<Student> onSon =thisParent.getSon().iterator();
    	while (onSon.hasNext()) {
    		Student i = onSon.next();
    		StudentResponse studentInterface = new StudentResponse();
    		studentInterface.setLastName(i.getLastName());
    		studentInterface.setName(i.getName());
    		studentInterface.setRollNo(i.getRollNo());
    		String uri=uriInfo.getBaseUriBuilder().path("parent").path(id).path("son").path(Long.toString(i.getRollNo())).build().toString();
        	studentInterface.addLink(uri, "self","GET");
        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
    				.resolveTemplate("user_id",id).build().toString();
        	studentInterface.addLink(uri, "general services","GET");
    		studentInterfaces.add(studentInterface);
    	}
    	GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(studentInterfaces) {};
    	if (thisParent.getUsername().equals(id)) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
	
	@GET
	@Path("son/{roll_id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response  sonByRollNum(@PathParam("user_id") String id,@PathParam("roll_id")int rollNum,@Context UriInfo uriInfo) {
    	StudentResponse studentInterface= null;
		Parent thisParent = parentDao.get(id);
    	Iterator<Student> onSon =thisParent.getSon().iterator();
    	while (onSon.hasNext()) {
    		Student i = onSon.next();
    		if (i.getRollNo()==rollNum) {
				studentInterface = new StudentResponse();
				studentInterface.setLastName(i.getLastName());
				studentInterface.setName(i.getName());
				studentInterface.setRollNo(i.getRollNo());
				String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		    	studentInterface.addLink(uri, "self","GET");
		    	studentInterface.addLink(uri, "modify self","PUT");
		    	uri=uriInfo.getBaseUriBuilder().path("parent").path(id).
		    			path("son").path(Long.toString(i.getRollNo())).path("marks").build().toString();
		    	studentInterface.addLink(uri, "marks","GET");
		    	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	studentInterface.addLink(uri, "general services","GET");
    		}
    	}
    	if (studentInterface!=null) {
            return Response.status(Response.Status.OK).entity(studentInterface).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
	@PUT
	@Path("son/{roll_id}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response setSonPersonal(@PathParam("user_id") String id,@PathParam("roll_id")int rollNum,Wrapper sonIn,@Context UriInfo uriInfo) {
		Parent parent= parentDao.get(id);
		
		Iterator<Student> childrenOf = parent.getSon().iterator();
		while (childrenOf.hasNext()) {
			Student s =childrenOf.next();
			if (s.getRollNo()==sonIn.getRollNo() && rollNum==sonIn.getRollNo()) {
				s.setLastName(sonIn.getLastName());
				s.setName(sonIn.getName());
				List<Link> uris = new ArrayList<Link>();
				String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				addLinkToList(uris, uri, "see self", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
				GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
				if (studentDao.update(s)) {
		            return Response.status(Response.Status.OK).entity(e).build();
		        }
		        else {
		            return Response.status(Response.Status.BAD_REQUEST).build();
		        }
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();	 
	}
	
	//DA CONTROLLARE
	@Path("son/{son_id}/marks")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response getMarkSon(@PathParam("user_id") String id,@PathParam("son_id") int n_stud,@Context UriInfo uriInfo) {
		List< Course_ClassResponse> newListofResp = new ArrayList<Course_ClassResponse>();
		List<Course_Class> allCourse = course_classDao.findAll();
		System.out.println("size di course class: "+ allCourse.size());
        Parent thisParent = parentDao.get(id);
        Iterator<Student> onSon =thisParent.getSon().iterator();
        Course_ClassResponse newResp= new Course_ClassResponse();
        	while (onSon.hasNext()) {
        		Student i = onSon.next();
        		
        		for(Course_Class cc : allCourse) {
        		
        			System.out.println("idStud " + i.getRollNo());
        			if (i.getRollNo()==cc.getId().getStudentId().getRollNo()) {
        				newResp=new Course_ClassResponse();
        				newResp.getCw().setCourseName(cc.getId().getCourseId().getCourseName());
        				newResp.getSw().setLastName(i.getLastName());
        				newResp.setMark(cc.getMark());
        				String uri= uriInfo.getAbsolutePathBuilder().build().toString();
        				newResp.addLink(uri, "self", "GET");
        				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
        	    				.resolveTemplate("user_id",id).build().toString();
        	        	newResp.addLink(uri, "general services","GET");
        				newListofResp.add(newResp);		
        			}
        		}
        	}
        	 GenericEntity<List<Course_ClassResponse>> e = new GenericEntity<List<Course_ClassResponse>>(newListofResp) {};
             if (newListofResp.size()>0) {
                 return Response.status(Response.Status.OK).entity(e).build();
             }
             else {
                 return Response.status(Response.Status.BAD_REQUEST).entity("list empty").build();
             }
	}
    
    //forse da mettere opzioni nella put
	@Path("appointments")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getAppointment(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		
		List< AppointmentResponse> ds = new ArrayList<AppointmentResponse>();
        List<Appointment> apps = appointmentDao.findAll();
        for(Appointment a : apps) {
        	if(id.equals(a.getId().getParents().getUsername())) {
        		AppointmentResponse appResp= new AppointmentResponse();
        		appResp.setParentUsername(a.getId().getParents().getUsername());
        		appResp.setTeacherId(a.getId().getTeacher().getTeacherId());
        		appResp.setAppointmentDate(a.getId().getAppointmentDate());
        		String uri= uriInfo.getAbsolutePathBuilder().build().toString();
				appResp.addLink(uri, "self", "GET");
				uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
						.resolveTemplate("user_id",appResp.getParentUsername()).build().toString();
				appResp.addLink(uri, "new appointment", "POST");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).resolveTemplate("user_id",id).path("appointments").build().toString();
				appResp.addLink(uri, "modify self", "PUT");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	appResp.addLink(uri, "general services","GET");
				ds.add(appResp);
        	}
        }
        GenericEntity<List<AppointmentResponse>> e = new GenericEntity<List<AppointmentResponse>>(ds) {};
        if (apps!=null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
	//da sistmeare appointment in toto
	//@PAth("appointments/appointment")
	@Path("appointments")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response  setAppointment(@PathParam("user_id") String id,AppointmentWrapper upApp,@Context UriInfo uriInfo) {
		if (id .equals(upApp.getParentUsername())){
			Appointment a=deleteOldApp(upApp, id);
			if (a!=null) {
				Appointment newApp = new Appointment();
				newApp.setId(new Appointment_Id());
				newApp.getId().setAppointmentDate(upApp.getAppointmentDate());
				newApp.getId().setParents(a.getId().getParents());
				newApp.getId().setTeacher(a.getId().getTeacher());
				List<Link> uris = new ArrayList<Link>();
				String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				addLinkToList(uris, uri, "see self", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
				GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
				if (appointmentDao.create(newApp))
					return Response.status(Response.Status.OK).entity(e).build();
				else
					return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
    }
	
	
	public Appointment deleteOldApp(AppointmentWrapper oldApp,String id) {
        List<Appointment> apps = appointmentDao.findAll();
        //Date oldD= oldApp.getAppointmentDate();
        String parent=oldApp.getParentUsername();
        String teacherId=oldApp.getTeacherId();
        for(Appointment a : apps) {
        	if(id.equals(parent) && a.getId().getTeacher().getTeacherId().equals(teacherId)) {
        		if (appointmentDao.delete(a)) return a;
        		else return null;
        	}
        }
		return null;
	}
	
	@Path("History")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getPaymentHistory(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		
		List<Payement> ds = paymentDao.findAll();
		List<PaymentResponse> newDs = new ArrayList<PaymentResponse>();
		
		//poi sostituire con un metodo abstract che ritorna List<Payemnt>
        for(Payement p : ds) {
        	//System.out.println(p.parentUsername);
        	//sistemare l'if con contesto notification date (?)
    		if (p.getParentUsername().equals(id) && p.isPayed()==true) {
    			PaymentResponse ps = new PaymentResponse();
    			ps.setPayID(p.getPayID());
    			ps.setParentUsername(p.getParentUsername());
    			ps.setPaymentDescription(p.getPaymentDescription());
    			ps.setCost(p.getCost());
    			ps.setPayed(p.isPayed());
    			ps.setNotificationDate(p.getNotificationDate());
    			ps.setPayementDate(p.getPayementDate());
    			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				ps.addLink( uri, "self", "GET");
				uri= uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).path("Payments").build().toString();
				ps.addLink(uri, "Upcoming Payments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	ps.addLink(uri, "general services","GET");
    			newDs.add(ps);
    		}
    	}
        GenericEntity<List<PaymentResponse>> e = new GenericEntity<List<PaymentResponse>>(newDs) {};
        if (ds!=null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    
	}
	
	@Path("Payments")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response getUpcomingPayment(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		List<Payement> ds = new ArrayList<Payement>();
		List<PaymentResponse> newDs = new ArrayList<PaymentResponse>();
		//poi sostituire con un metodo abstract che ritorna List<Payemnt>
		ds=paymentDao.findAll();
        for(Payement p : ds) {
        	System.out.println(p.toString());
        	System.out.println(id);
        	System.out.println(p.getParentUsername());
    		if (p.getParentUsername().equals(id) && p.isPayed()==false) {
    			PaymentResponse ps = new PaymentResponse();
    			ps.setPayID(p.getPayID());
    			ps.setParentUsername(p.getParentUsername());
    			ps.setPaymentDescription(p.getPaymentDescription());
    			ps.setCost(p.getCost());
    			ps.setPayed(p.isPayed());
    			ps.setNotificationDate(p.getNotificationDate());
    			ps.setPayementDate(p.getPayementDate());
    			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				ps.addLink( uri, "self", "GET");
				uri=uriInfo.getAbsolutePathBuilder().path(Long.toString(ps.getPayID())).build().toString();
				ps.addLink( uri, "pay this payment", "PUT");
				uri= uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).path("History").build().toString();
				ps.addLink(uri, "history payments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	ps.addLink(uri, "general services","GET");
    			newDs.add(ps);
    		}
    	}
        GenericEntity<List<PaymentResponse>> e = new GenericEntity<List<PaymentResponse>>(newDs) {};
        if (ds!=null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    	
    }
	
	//@Path("payments/payment/{pay_id}")
	@Path("Payments/{pay_id}")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response  setPay(@PathParam("user_id") String id,@PathParam("pay_id") int payId,
    		Payement pw,@Context UriInfo uriInfo) {
		List<Payement> ds = new ArrayList<Payement>();
		//poi sostituire con un metodo abstract che ritorna List<Payemnt>
		ds=paymentDao.findAll();
        for(Payement p : ds) {
        	System.out.println(p.toString());
    		if (p.getParentUsername().equals(pw.getParentUsername()) && p.isPayed()==false &&
    				p.getPayID()==pw.getPayID() && payId==pw.getPayID()) {
    			p.setPayed(true);
    			GregorianCalendar c = new GregorianCalendar();
    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    			Date now = new Date();
    		    String strDate = sdf.format(now);
    		    try {
					c.setTime(sdf.parse(strDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		    List<Link> uris = new ArrayList<Link>();
    			p.setPayementDate(c.getTime());
    			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
							.resolveTemplate("user_id",id).path("History").build().toString();
				addLinkToList(uris, uri, "History", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).path("Payments").build().toString();
				addLinkToList(uris, uri, "Upcoming Payments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
				GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
    			if (paymentDao.update(p)) {
		            return Response.status(Response.Status.OK).entity(e).build();
		        }
		        else {
		            return Response.status(Response.Status.BAD_REQUEST).build();
		        }
    		}
    	}
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
	
	
   
   	@Path("pubNotif")
	@GET
	@Produces(MediaType.APPLICATION_XML)
   public Response  getPublicNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
       	List<Notificatio>noteList = notificationDao.findAll();
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getUserName().equals(id) && n.getContentType().equals("PUBLIC")) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setUserName(n.getPrimaryKey().getUserName());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri= uriInfo.getAbsolutePathBuilder().build().toString();
           		newNot.addLink(uri, "self", "GET");
           		uri = uriInfo.getBaseUriBuilder().path(ParentResource.class)
           				.resolveTemplate("user_id",id).path("privNotif").build().toString();
           		newNot.addLink(uri, "private Notifications", "GET");
           		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	newNot.addLink(uri, "general services","GET");
       			newList.add(newNot);
       			
       		}
       	}
        GenericEntity<List<NotificationResponse>> e = new GenericEntity<List<NotificationResponse>>(newList) {};
        if (noteList!=null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
       	
   }
   	//utile singola visuale per vedere le notifiche?
 	@Path("privNotif")
	@GET
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
   public Response  getPrivateNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
       	List<Notificatio>noteList =  notificationDao.findAll();
       	System.out.println(noteList);
       	System.out.println(id);
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getUserName().equals(id) && n.getContentType().equals("PRIVATE")) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setUserName(n.getPrimaryKey().getUserName());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri= uriInfo.getAbsolutePathBuilder().build().toString();
           		newNot.addLink(uri, "self", "GET");
           		uri = uriInfo.getBaseUriBuilder().path(ParentResource.class)
           				.resolveTemplate("user_id",id).path("pubNotif").build().toString();
           		newNot.addLink(uri, "public Notifications", "GET");
           		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	newNot.addLink(uri, "general services","GET");
       			newList.add(newNot);
       		
       		}
       	}
        GenericEntity<List<NotificationResponse>> e = new GenericEntity<List<NotificationResponse>>(newList) {};
        if (noteList!=null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
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

	@Override
	public String getName() {
		
		System.out.println(uriInfo.getBaseUri().toString());
		
		// TODO Auto-generated method stub
		return null;
	}
}
