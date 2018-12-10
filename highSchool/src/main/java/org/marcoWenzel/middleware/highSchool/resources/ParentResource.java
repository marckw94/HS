package org.marcoWenzel.middleware.highSchool.resources;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.persistence.exceptions.i18n.DatabaseExceptionResource;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.PaymentDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.exception.NoContentException;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Evaluation;
import org.marcoWenzel.middleware.highSchool.model.Notificatio;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Payement;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.response.AppointmentResponse;
import org.marcoWenzel.middleware.highSchool.response.EvaluationResponse;
import org.marcoWenzel.middleware.highSchool.response.NotificationResponse;
import org.marcoWenzel.middleware.highSchool.response.ParentResponse;
import org.marcoWenzel.middleware.highSchool.response.PaymentResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.AppointmentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.EvaluationWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.NotificationWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.ParentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.PayRequest;
import org.marcoWenzel.middleware.highSchool.wrapper.PaymentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Wrapper;
@Secured({Category.Parent})
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Path("Parent/{user_id}")
//cuiao
public class ParentResource{
	ParentDAO parentDao = new ParentDAO();
	StudentDAO studentDao = new StudentDAO();
	EvaluationDAO course_classDao = new EvaluationDAO();
	AppointmentDAO appointmentDao = new AppointmentDAO();
	NotificationDAO notificationDao = new NotificationDAO();
	PaymentDAO paymentDao = new PaymentDAO();
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
    
	@Context 
	UriInfo uriInfo;
	@GET
	public Response getParentServices(@PathParam("user_id")String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("personal")
				.build().toString();
		addLinkToList(uris, uri, "see personal", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("children")
				.build().toString();
		addLinkToList(uris, uri, "see children", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id).path("allAppointments")
				.build().toString();
		addLinkToList(uris, uri, "see all appointments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id)
				.build().toString();
		addLinkToList(uris, uri, "new appointment", "POST");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("Payment").path("allPaid")
				.build().toString();
		addLinkToList(uris, uri, "see all paid payments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("Payment").path("allPending")
				.build().toString();
		addLinkToList(uris, uri, "see all pending payments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("pubNotif")
				.build().toString();
		addLinkToList(uris, uri, "see public notifications", "GET");
		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
				.resolveTemplate("user_id",id).path("privNotif")
				.build().toString();
		addLinkToList(uris, uri, "see private notifications", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	}
	
	@Path("personal")
	@GET

    public Response  getPersonalData(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
	
		
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
        	uri = uriInfo.getBaseUriBuilder().path(ParentResource.class).path(id).path("children").build().toString();
        	parentInterface.addLink(uri, "see children","GET");
        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
    				.resolveTemplate("user_id",id).build().toString();
        	parentInterface.addLink(uri, "general services","GET");
        	if (thisParent.getUsername().equals(id)) {
	            return Response.status(Response.Status.OK).entity(parentInterface).type(negotiation(h)).build();
	        }
	        else {
	        	 throw new DataNotFoundException();
	        	 }
        
    }
//triggerare nel caso di password	
	@PUT
	@Path("personal")

	public Response setParentPersonal(ParentWrapper parIn,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
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
	            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	        }
	        else {
	        	 throw new DataNotFoundException();
	        	 }
	}
	
	@GET
	@Path("children")

    public Response  getSonsList(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<StudentResponse> studentInterfaces = new ArrayList<StudentResponse>();
    	Parent thisParent = parentDao.get(id);
    	Iterator<Student> onSon =thisParent.getSon().iterator();
    	if (!thisParent.getUsername().equals(id)) 
    		 throw new DataNotFoundException();
    	while (onSon.hasNext()) {
    		Student i = onSon.next();
    		StudentResponse studentInterface = new StudentResponse();
    		studentInterface.setLastName(i.getLastName());
    		studentInterface.setName(i.getName());
    		studentInterface.setRollNo(i.getRollNo());
    		String uri;
    		if (studentInterfaces.isEmpty()) {
	    		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).path(id).path("son").path(Long.toString(i.getRollNo())).build().toString();
	        	studentInterface.addLink(uri, "self","GET");
	        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	studentInterface.addLink(uri, "general services","GET");
    		}
    		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
    				.resolveTemplate("user_id",id).path("child").path(String.valueOf(studentInterface.getRollNo()))
    				.build().toString();
    		studentInterface.addLink(uri, "see child in specific","GET");
    		studentInterfaces.add(studentInterface);
    	}
    	GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(studentInterfaces) {};
    	if (!studentInterfaces.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();        }
    }
	
	@GET
	@Path("child/{roll_id}")

    public Response  sonByRollNum(@PathParam("user_id") String id,@PathParam("roll_id")int rollNum,@Context HttpHeaders h,@Context UriInfo uriInfo) {
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
		    	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).path(id).
		    			path("child").path(Long.toString(i.getRollNo())).path("marks").build().toString();
		    	studentInterface.addLink(uri, "marks","GET");
		    	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).path(id).
		    			path("children").build().toString();
		    	studentInterface.addLink(uri, "see all children of this parent","GET");
		    	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	studentInterface.addLink(uri, "general services","GET");
    		}
    	}
    	if (studentInterface!=null) {
            return Response.status(Response.Status.OK).entity(studentInterface).type(negotiation(h)).build();
        }
        else {
             throw new DataNotFoundException();
        }
    }
	@PUT
	@Path("child/{roll_id}")

	public Response setSonPersonal(@PathParam("user_id") String id,@PathParam("roll_id")int rollNum,Wrapper sonIn,
			@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Parent parent= parentDao.get(id);
		
		Iterator<Student> childrenOf = parent.getSon().iterator();
		while (childrenOf.hasNext()) {
			Student s =childrenOf.next();
			if (s.getRollNo()==rollNum) {
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
		            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		        }
		        else {
		        	 throw new DataNotFoundException();
		        	 }
			}
		}
		 throw new DataNotFoundException();
		 }
	
	@Path("child/{son_id}/marks")
	@GET

    public Response getMarkSon(@PathParam("user_id") String id,@PathParam("son_id") int n_stud,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List< EvaluationResponse> newListofResp = new ArrayList<EvaluationResponse>();
		List<Evaluation> allCourse = course_classDao.findAll();
		System.out.println("size di course class: "+ allCourse.size());
        Parent thisParent = parentDao.get(id);
        Iterator<Student> onSon =thisParent.getSon().iterator();
        EvaluationResponse newResp= new EvaluationResponse();
        	while (onSon.hasNext()) {
        		Student i = onSon.next();
        		
        		for(Evaluation cc : allCourse) {
        		
        			System.out.println("idStud " + i.getRollNo());
        			if (i.getRollNo()==cc.getId().getStudentId().getRollNo()) {
        				newResp=new EvaluationResponse();
        				newResp.setMark(cc.getMark());
        				String uri= uriInfo.getAbsolutePathBuilder().build().toString();
        				newResp.addLink(uri, "self", "GET");
        				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
        	    				.resolveTemplate("user_id",id).build().toString();
        	        	newResp.addLink(uri, "general services","GET");
        				newListofResp.add(newResp);
        				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).path(id).
        		    			path("children").build().toString();
        		    	newResp.addLink(uri, "see all children of this parent","GET");
        			}
        		}
        	}
        	 GenericEntity<List<EvaluationResponse>> e = new GenericEntity<List<EvaluationResponse>>(newListofResp) {};
             if (newListofResp.size()>0) {
                 return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
             }
             else {
            	 throw new NoContentException();
            	 }
	}
    
/*
	@Path("allAppointments")
	@GET

    public Response  getAppointment(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
		List< AppointmentResponse> ds = new ArrayList<AppointmentResponse>();
		List<Appointment> apps = appointmentDao.findAll();
        for(Appointment a : apps) {
        	if(id.equals(a.getParentId())) {
        		AppointmentResponse appResp = new AppointmentResponse();
        		appResp.setAppointmentId(a.getAppointId());
        		appResp.setParentUsername(a.getParentId());
        		appResp.setTeacherId(a.getTeacherId());
        		appResp.setAppointmentDate(a.getAppointmentDate());
        		String uri= uriInfo.getAbsolutePathBuilder().build().toString();
        		appResp.addLink(uri, "self", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class).resolveTemplate("user_id", id)
        				.path("setAppointments").path(appResp.getParentUsername()).build().toString();
        		appResp.addLink(uri, "modify appointment", "PUT");
        		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id).build().toString();
        		appResp.addLink(uri, "new appointment", "POST");
        		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
        				.resolveTemplate("user_id",id).build().toString();
            	appResp.addLink(uri, "general services","GET");
        		ds.add(appResp);
        	}
        }
        GenericEntity<List<AppointmentResponse>> e = new GenericEntity<List<AppointmentResponse>>(ds) {};
        if (ds.size()>0) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
        
    }
	
	//sistemaere struttura tabella appointment
	@PUT
	@Path("setAppointments/{appoint_id}")

    public Response  setAppointment(@PathParam("user_id") String parentid,@PathParam("appoint_id") int appId,
    		AppointmentWrapper upApp,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Appointment oldApp= appointmentDao.get(appId);
		
		if(oldApp==null)
			 throw new DataNotFoundException();
		if(upApp.getParentUsername().equals(oldApp.getParentId()) && upApp.getTeacherId().equals(oldApp.getTeacherId())
				&& upApp.getParentUsername().equals(parentid)) {
			oldApp.setAppointmentDate(upApp.getAppointmentDate());
			List<Link> uris = new ArrayList<Link>();
			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
			addLinkToList(uris, uri, "self", "PUT");
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id", parentid).path("appointments")
					.build().toString();
			addLinkToList(uris, uri, "see appointments", "GET");
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id",parentid).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
			GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
			if(appointmentDao.update(oldApp))
				return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
			else
				 throw new DataNotFoundException();
			}

		 throw new DataNotFoundException();
		 }
	
	@DELETE
	@Path("setAppointments/{appoint_id}")

    public Response  deleteAppointment(@PathParam("user_id") String teacherid,@PathParam("appoint_id") int AppId,
    		AppointmentWrapper upApp,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Appointment oldApp= appointmentDao.get(AppId);
		if(oldApp==null)
			 throw new DataNotFoundException();
		if(upApp.getParentUsername().equals(oldApp.getParentId()) && upApp.getTeacherId().equals(oldApp.getTeacherId())
				&& upApp.getTeacherId().equals(teacherid)) {
			List<Link> uris = new ArrayList<Link>();
			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
			addLinkToList(uris, uri, "self", "PUT");
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id", teacherid).path("appointments")
					.build().toString();
			addLinkToList(uris, uri, "see appointments", "GET");
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id",teacherid).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
			GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
			if(appointmentDao.delete(oldApp))
				return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
			else
				 throw new DataNotFoundException();
			}

		 throw new DataNotFoundException();
		 }
		 
		 
*/
	@Path("Payment/allPaid")
	@GET

    public Response  getPaymentHistory(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
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
    			String uri;
    			if (newDs.isEmpty()) {
	    			uri=uriInfo.getAbsolutePathBuilder().build().toString();
					ps.addLink( uri, "self", "GET");
					uri= uriInfo.getBaseUriBuilder().path(ParentResource.class)
							.resolveTemplate("user_id",id).path("Payment").path("allPending").build().toString();
					ps.addLink(uri, "see pending payments", "GET");
					uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
		    				.resolveTemplate("user_id",id).build().toString();
		        	ps.addLink(uri, "general services","GET");
    			}
    			newDs.add(ps);
    		}
    	}
        GenericEntity<List<PaymentResponse>> e = new GenericEntity<List<PaymentResponse>>(newDs) {};
        if (!newDs.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
    
	}
	
	@Path("Payment/allPending")
	@GET

    public Response getUpcomingPayment(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
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
    			String uri;
    			if(newDs.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
					ps.addLink( uri, "self", "GET");
					
					uri= uriInfo.getBaseUriBuilder().path(ParentResource.class)
							.resolveTemplate("user_id",id).path("Payment").path("allPaid").build().toString();
					ps.addLink(uri, "see all paid payments", "GET");
					uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
		    				.resolveTemplate("user_id",id).build().toString();
		        	ps.addLink(uri, "general services","GET");
    			}
    			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("Payment").path(String.valueOf(ps.getPayID()))
	    				.build().toString();
				ps.addLink( uri, "pay this payment", "PUT");
    			newDs.add(ps);
    		}
    	}
        GenericEntity<List<PaymentResponse>> e = new GenericEntity<List<PaymentResponse>>(newDs) {};
        if (!newDs.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
    	
    }
	
	//@Path("payments/payment/{pay_id}")
	@Path("Payment/{pay_id}")
	@PUT

    public Response  setPay(@PathParam("user_id") String id,@PathParam("pay_id") int payId,
    		PayRequest pw,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Payement> ds = new ArrayList<Payement>();
		//poi sostituire con un metodo abstract che ritorna List<Payemnt>
		ds=paymentDao.findAll();
        for(Payement p : ds) {
        	System.out.println(p.toString());
    		if (p.getParentUsername().equals(pw.getParentUsername()) && p.isPayed()==false &&
    				p.getPayID()==pw.getPayID() && payId==pw.getPayID()) {
    			p.setPayed(true);
    		    List<Link> uris = new ArrayList<Link>();
    			p.setPayementDate(cal.getTime());
    			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
							.resolveTemplate("user_id",id).path("allPaid").build().toString();
				addLinkToList(uris, uri, "see all paid payments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).path("allPending").build().toString();
				addLinkToList(uris, uri, "see all pending Payments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
				GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
    			if (paymentDao.update(p)) {
		            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		        }
		        else {
		        	 throw new DataNotFoundException();
		        	 }
    		}
    	}
        throw new DataNotFoundException();
        }
	
	
   
   	@Path("pubNotif")
	@GET

   public Response  getPublicNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
       	List<Notificatio>noteList = notificationDao.findAll();
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getReceiver().equals(id) && n.getContentType().equals("PUBLIC")) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setReceiver(n.getPrimaryKey().getReceiver());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri;
       			if (newList.isEmpty()) {
       				uri= uriInfo.getAbsolutePathBuilder().build().toString();
	           		newNot.addLink(uri, "self", "GET");
	           		uri = uriInfo.getBaseUriBuilder().path(ParentResource.class)
	           				.resolveTemplate("user_id",id).path("privNotif").build().toString();
	           		newNot.addLink(uri, "private Notifications", "GET");
	           		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
		    				.resolveTemplate("user_id",id).build().toString();
		        	newNot.addLink(uri, "general services","GET");
       			}
       			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("Notification").path(String.valueOf(newNot.getNotificationNumber()))
	    				.build().toString();
	        	newNot.addLink(uri, "general services","GET");
       			newList.add(newNot);
       			
       		}
       	}
        GenericEntity<List<NotificationResponse>> e = new GenericEntity<List<NotificationResponse>>(newList) {};
        if (!newList.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
       	
   }
   	
 	@Path("privNotif")
	@GET

   public Response  getPrivateNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
       	List<Notificatio>noteList =  notificationDao.findAll();
       	System.out.println(noteList);
       	System.out.println(id);
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getReceiver().equals(id) && n.getContentType().equals("PRIVATE")) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setReceiver(n.getPrimaryKey().getReceiver());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri;
       			if (newList.isEmpty()) {
       				uri= uriInfo.getAbsolutePathBuilder().build().toString();
	           		newNot.addLink(uri, "self", "GET");
	           		uri = uriInfo.getBaseUriBuilder().path(ParentResource.class)
	           				.resolveTemplate("user_id",id).path("pubNotif").build().toString();
	           		newNot.addLink(uri, "public Notifications", "GET");
	           		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
		    				.resolveTemplate("user_id",id).build().toString();
		        	newNot.addLink(uri, "general services","GET");
       			}
       			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("Notification").path(String.valueOf(newNot.getNotificationNumber()))
	    				.build().toString();
	        	newNot.addLink(uri, "general services","GET");
       			newList.add(newNot);
       		
       		}
       	}
        GenericEntity<List<NotificationResponse>> e = new GenericEntity<List<NotificationResponse>>(newList) {};
        if (!newList.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
       	
   }
 	@Path("Notification/{notification_id}")
	@GET
   public Response  getSingleNotif(@PathParam("user_id") String id,@PathParam("notification_id") int notifiD,@Context UriInfo uriInfo,@Context HttpHeaders h) {
       	List<Notificatio>noteList = notificationDao.findAll();
       	for(Notificatio n : noteList) {
       		if (n.getPrimaryKey().getNotificationNumber()==notifiD && 
       				n.getPrimaryKey().getReceiver().equals(id)) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setReceiver(n.getPrimaryKey().getReceiver());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri= uriInfo.getAbsolutePathBuilder().build().toString();
           		newNot.addLink(uri, "self", "GET");
           		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	newNot.addLink(uri, "general services","GET");
	        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("privNotif")
	    				.build().toString();
	        	newNot.addLink(uri, "see all private notifications","GET");
	        	uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("pubNotif")
	    				.build().toString();
	        	newNot.addLink(uri, "see all private notifications","GET");
	        	return Response.status(Response.Status.OK).entity(newNot).type(negotiation(h)).build();
       		}
       	}
       	throw new DataNotFoundException();
      
       	
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
