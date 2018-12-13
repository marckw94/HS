package org.marcoWenzel.middleware.highSchool.resources;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.exception.NoContentException;
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
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Path("Appoint/{user_id}")
public class AppointmentResource {
	AppointmentDAO appointmentDao = new AppointmentDAO();
	TeacherDAO teacherDao = new TeacherDAO();
	ParentDAO parentDao = new ParentDAO();
	CCAssotiationDAO ccaDao = new CCAssotiationDAO();
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	
	@Path("allAppointments")
	@GET
    public Response  getAppointment(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		int usRole= identificator(id);
		String reqUs;
		List< AppointmentResponse> ds = new ArrayList<AppointmentResponse>();
		List<Appointment> apps = appointmentDao.findAll();
        for(Appointment a : apps) {
        	if (usRole==1) {
    			reqUs=a.getParentId();
    		}else
    			reqUs=a.getTeacherId();
        	if(id.equals(reqUs)) {
        		AppointmentResponse appResp = new AppointmentResponse();
        		appResp.setAppointmentId(a.getAppointId());
        		appResp.setParentUsername(a.getParentId());
        		appResp.setTeacherId(a.getTeacherId());
        		appResp.setAppointmentDate(a.getAppointmentDate());
        		String uri;
        		
        		//Teacher
        		if(usRole==0) {
	        		
        			if(ds.isEmpty()) {
		        		
        				uri= uriInfo.getAbsolutePathBuilder().build().toString();
		        		appResp.addLink(uri, "self", "GET");
		           		
		        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
		        				.resolveTemplate("user_id",id).build().toString();
		            	appResp.addLink(uri, "general services","GET");
	        		}
        			
	        		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id)
	        				.path("Appointment").path(String.valueOf(appResp.getAppointmentId()))
	        				.build().toString();
	        		appResp.addLink(uri, "see specific appointment","GET");
	        		ds.add(appResp);
        		
        		}
        		//Parent
        		else {
        			
        			if(ds.isEmpty()) {
		        		
        				uri= uriInfo.getAbsolutePathBuilder().build().toString();
		        		appResp.addLink(uri, "self", "GET");
		           		
		        		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
		        				.resolveTemplate("user_id",id).build().toString();
		            	appResp.addLink(uri, "general services","GET");
	        		}
	        		
        			uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id)
	        				.path("Appointment").path(String.valueOf(appResp.getAppointmentId()))
	        				.build().toString();
	        		appResp.addLink(uri, "see specific appointment","GET");
	        		ds.add(appResp);
        		}
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
	
	
	@GET
	@Path("Appointment/{appoint_id}")
	 public Response  getAppointment(@PathParam("user_id") String id,@PathParam("appoint_id") int AppId,
	    	@Context UriInfo uriInfo,@Context HttpHeaders h) {
		int usRole= identificator(id);
		String reqUs;
		Appointment app= appointmentDao.get(AppId);
		if (app==null)
			throw new DataNotFoundException();
		if (usRole==1) {
			reqUs=app.getParentId();
		}else
			reqUs=app.getTeacherId();
		if (!reqUs.equals(id))
			throw new DataNotFoundException();
	
		AppointmentResponse aResp= new AppointmentResponse();
		aResp.setTeacherId(app.getTeacherId());
		aResp.setParentUsername(app.getParentId());
		aResp.setAppointmentDate(app.getAppointmentDate());
		aResp.setAppointmentId(app.getAppointId());
		
		String uri;
		//Teacher
		if(usRole==0) {
			
			uri=uriInfo.getAbsolutePathBuilder().build().toString();
			aResp.addLink(uri, "self", "GET");
			
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id",id).build().toString();
	    	aResp.addLink(uri, "general services","GET");
	    	
	    	uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
					.resolveTemplate("user_id",id)
					.path("allAppointments")
					.build().toString();
	    	aResp.addLink(uri, "all appointments","GET");
	        
	    	uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        aResp.addLink( uri, "modify this appointment", "PUT");
	        
	        uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id)
	        		.path("newAppointmment")
	        		.build().toString();
			aResp.addLink(uri, "new appointment", "POST");
	        
			uri=uriInfo.getAbsolutePathBuilder().build().toString();
			aResp.addLink(uri, "delete appointment", "DELETE");
			
		}
		//Parent
		else {
			
			uri=uriInfo.getAbsolutePathBuilder().build().toString();
			aResp.addLink(uri, "self", "GET");
			
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id",id).build().toString();
	    	aResp.addLink(uri, "general services","GET");
	    	
	    	uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
					.resolveTemplate("user_id",id)
					.path("allAppointments")
					.build().toString();
	    	aResp.addLink(uri, "all appointments","GET");
	    	
	    	uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        aResp.addLink( uri, "modify this appoinment", "PUT");
	        
	        uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id)
	        		.path("newAppointmment")
	        		.build().toString();
			aResp.addLink(uri, "new appointment", "POST");
	        
			uri=uriInfo.getAbsolutePathBuilder().build().toString();
			aResp.addLink(uri, "delete appointment", "DELETE");
		}
		return Response.status(Response.Status.OK).entity(aResp).type(negotiation(h)).build();
		
	}
	
	@POST
	@Path("newAppointment")
    public Response newAppointment(@PathParam("user_id")String userId,AppointmentWrapper aw,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		String parent=aw.getParentUsername();
		String teacher=aw.getTeacherId();
		Parent parentObj=parentDao.get(parent);
		
		Teacher teacherObj= teacherDao.get(teacher);
		int validityCheck=0;
		
		if (parentObj==null || teacherObj==null)
			 throw new DataNotFoundException();
		List<Classes>enrolledSonClass= new ArrayList<Classes>();
		for(Student s:parentObj.getSon()) {
			enrolledSonClass.add(s.getEnrolledClass());
		}

		List<CourseClassAssociation> ccaList = ccaDao.findAll();
		List<Integer> courseFollowSons= new ArrayList<Integer>();
		
		for(CourseClassAssociation cca: ccaList) {
			int classId=cca.getPrimaryKey().getClass_id();
			for(Classes e : enrolledSonClass) {
				if(classId==e.getIdClass())
					 courseFollowSons.add(classId);
			}	
		}

		for(Course c :teacherObj.getCourseKeep()) {
			if (courseFollowSons.contains(c.getIdCourse()))
				validityCheck=1;
		}
		
		if (validityCheck==0)
			 throw new DataNotFoundException();				
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
			
			uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
			.resolveTemplate("user_id", userId).path("allAppointments").build().toString();
			addLinkToList(uris, uri, "see all appointments", "GET");
			
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id",userId).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
	        
	        uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
					.resolveTemplate("user_id",userId).path("Appointment").path(String.valueOf(maxid)).
					build().toString();
	        addLinkToList(uris, uri, "see specific appointment", "GET");
	        
		}else if (aw.getParentUsername().equals(userId))  {
			
			uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
					.resolveTemplate("user_id", userId).path("allAppointments").build().toString();
			addLinkToList(uris, uri, "see all appointments", "GET");
			
			uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id",userId).build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
	        
	        uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
					.resolveTemplate("user_id",userId).path("Appointment").path(String.valueOf(maxid)).
					build().toString();
	        addLinkToList(uris, uri, "see specific appointment", "GET");
	        
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
	
	@PUT
	@Path("Appointment/{appoint_id}")
    public Response  setAppointment(@PathParam("user_id") String id,@PathParam("appoint_id") int AppId,
    		AppointmentWrapper upApp,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		int usRole= identificator(id);
		Appointment oldApp= appointmentDao.get(AppId);
		String reqUs;
		if(oldApp==null)
			 throw new DataNotFoundException();
		if (usRole==1) {
			reqUs=upApp.getParentUsername();
		}else
			reqUs=upApp.getTeacherId();
		
		if(upApp.getParentUsername().equals(oldApp.getParentId()) && upApp.getTeacherId().equals(oldApp.getTeacherId())
				&& reqUs.equals(id)) {
			oldApp.setAppointmentDate(upApp.getAppointmentDate());
			List<Link> uris = new ArrayList<Link>();
			String uri;
			//Teacher
			if(usRole==0) {
				
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				
				uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
						.resolveTemplate("user_id", id).path("allAppointments")
						.build().toString();
				addLinkToList(uris, uri, "see  all appointments", "GET");
				
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
			
			}else {
				
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				
				uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
						.resolveTemplate("user_id", id).path("allAppointments")
						.build().toString();
				addLinkToList(uris, uri, "see  all appointments", "GET");
				
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
			
			}
			
			GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
			if(appointmentDao.update(oldApp))
				return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
			else
				 throw new DataNotFoundException();
			}

		 throw new DataNotFoundException();
	}
	
	@DELETE
	@Path("Appointment/{appoint_id}")
    public Response  deleteAppointment(@PathParam("user_id") String id,@PathParam("appoint_id") int AppId,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		int usRole= identificator(id);
		Appointment oldApp= appointmentDao.get(AppId);
		String reqUs;
		if(oldApp==null)
			 throw new DataNotFoundException();
		if (usRole==1) {
			reqUs=oldApp.getParentId();
		}else
			reqUs=oldApp.getTeacherId();
		if(id.equals(reqUs)) {
			List<Link> uris = new ArrayList<Link>();
			//Teacher
			if(usRole==0) {
				String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "DELETE");
				
				uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
						.resolveTemplate("user_id", id).path("allAppointments")
						.build().toString();
				addLinkToList(uris, uri, "see all appointments", "GET");
				
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
			
			}
			//Parent
			else {
				String uri=uriInfo.getAbsolutePathBuilder().build().toString();
				addLinkToList(uris, uri, "self", "PUT");
				
				uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
						.resolveTemplate("user_id", id).path("allAppointments")
						.build().toString();
				addLinkToList(uris, uri, "see all appointments", "GET");
				
				uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
			}
			GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
			if(appointmentDao.delete(oldApp))
				return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
			else
				 throw new DataNotFoundException();
			}

		 throw new DataNotFoundException();
		 }
	public int identificator(String user) {
		Parent parent = parentDao.get(user);
		if (parent!=null)
			return 1;
		Teacher teacher = teacherDao.get(user);
		if (teacher != null)
			return 0;
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
