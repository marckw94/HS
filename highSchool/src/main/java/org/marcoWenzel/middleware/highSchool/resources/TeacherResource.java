package org.marcoWenzel.middleware.highSchool.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.marcoWenzel.middleware.highSchool.dao.AbstractDAO;
import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.dao.TimeTableDAO;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Appointment_Id;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation;
import org.marcoWenzel.middleware.highSchool.model.Evaluation;
import org.marcoWenzel.middleware.highSchool.model.Evaluation_Id;
import org.marcoWenzel.middleware.highSchool.model.Notificatio;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.model.Teacher;
import org.marcoWenzel.middleware.highSchool.model.TimeTable;
import org.marcoWenzel.middleware.highSchool.response.AppointmentResponse;
import org.marcoWenzel.middleware.highSchool.response.CourseResponse;
import org.marcoWenzel.middleware.highSchool.response.EvaluationResponse;
import org.marcoWenzel.middleware.highSchool.response.NotificationResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.response.TeacherResponse;
import org.marcoWenzel.middleware.highSchool.response.TimeTableResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.AppointmentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.CourseWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.EvaluationWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.ParentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.TeacherWrapper;
@Secured({Category.Teacher})
@Path("teacher/{user_id}")
public class TeacherResource {
	
	TeacherDAO teacherDao = new TeacherDAO();	
	AppointmentDAO appointmentDao = new AppointmentDAO();
	TimeTableDAO timetableDao = new TimeTableDAO();
	CourseDAO courseDao = new CourseDAO();
	EvaluationDAO course_classDao = new EvaluationDAO();
	CCAssotiationDAO ccaDao = new CCAssotiationDAO();
	StudentDAO studentDao=new StudentDAO();
	NotificationDAO notificationDao= new NotificationDAO();
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getTeacherServices(@PathParam("user_id")String id,@Context UriInfo uriInfo) {
		
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("personal")
				.build().toString();
		addLinkToList(uris, uri, "see personal", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("Classes")
				.build().toString();
		addLinkToList(uris, uri, "see classes", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("appointments")
				.build().toString();
		addLinkToList(uris, uri, "see appointments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id)
				.build().toString();
		addLinkToList(uris, uri, "new appointment", "POST");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).build();
	}
	@Path("personal")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getPersonalData(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
        	Teacher thisTeacher = teacherDao.get(id);
        	TeacherResponse teacherResponse = new TeacherResponse();
        	teacherResponse.setName(thisTeacher.getName());
        	teacherResponse.setSurname(thisTeacher.getSurname());
        	teacherResponse.setTeacherId(thisTeacher.getTeacherId());
        	String uri = uriInfo.getAbsolutePathBuilder().build().toString();
        	teacherResponse.addLink(uri, "self", "GET");
        	uri = uriInfo.getAbsolutePathBuilder().build().toString();
        	teacherResponse.addLink(uri, "modify data", "PUT");
        	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        			.resolveTemplate("user_id", id).path("Classes").build().toString();
        	teacherResponse.addLink(uri, "classes", "GET");
        	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
    				.resolveTemplate("user_id",id).build().toString();
        	teacherResponse.addLink(uri, "general services","GET");
        	if (thisTeacher.getTeacherId().equals(id)) {
	            return Response.status(Response.Status.OK).entity(teacherResponse).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
        	
    }
	
// vedere cosa cambia per triggerare altre query
	@PUT
	@Path("personal")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response setTeacherPersonal(TeacherWrapper teaIn,@Context UriInfo uriInfo) {
		
		Teacher updateTeacher = teacherDao.get(teaIn.getTeacherId());
		updateTeacher.setName(teaIn.getName());
		updateTeacher.setSurname(teaIn.getSurname());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "PUT");
		addLinkToList(uris, uri, "see self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",updateTeacher.getTeacherId()).build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (teacherDao.update(updateTeacher)) {
	            return Response.status(Response.Status.OK).entity(e).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	}
	//sistemare nomenclatura
	@GET
	@Path("Courses")
	@Produces(MediaType.APPLICATION_XML)
    public Response  getClass(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		List<CourseResponse> teachedClass = new ArrayList<CourseResponse>();
        	Teacher thisTeacher = teacherDao.get(id);
        	Iterator<Course> onCourse =thisTeacher.getCourseKeep().iterator();
        	while (onCourse.hasNext()) {
        		Course i = onCourse.next();
        		CourseResponse newClass = new CourseResponse();
        		newClass.setClassRoom(i.getClassRoom());
        		newClass.setCourseDescription(i.getCourseDescription());
        		newClass.setCourseName(i.getCourseName());
        		newClass.setIdCourse(i.getIdCourse());
        		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
        		newClass.addLink(uri, "self", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
            			.resolveTemplate("user_id", id).path("Classes")
            			.path(Long.toString(newClass.getIdCourse())).path("students")
            			.build().toString();
        		newClass.addLink(uri, "class componets", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
            			.resolveTemplate("user_id", id).path("Classes")
            			.path(Long.toString(newClass.getIdCourse())).path("timeTable")
            			.build().toString();
        		newClass.addLink(uri, "timeTable", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        				.resolveTemplate("user_id",id).build().toString();
            	newClass.addLink(uri, "general services","GET");
        		teachedClass.add(newClass);
        	}
        	GenericEntity<List<CourseResponse>> e = new GenericEntity<List<CourseResponse>>(teachedClass) {};
        	if (thisTeacher.getTeacherId().equals(id)) {
                return Response.status(Response.Status.OK).entity(e).build();
            }
            else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
    }

	@Path("Classes/{course_id}/students")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getAllStudInClass(@PathParam("user_id") String idName,@PathParam("course_id") int id,@Context UriInfo uriInfo) {
        	Teacher subject = teacherDao.get(idName);
        	List<Integer> teachedClasses = new ArrayList<Integer>();
        	List<CourseClassAssociation>allAssotiation=ccaDao.findAll();
        	for(CourseClassAssociation cca:allAssotiation) {
        		for(Course c :subject.getCourseKeep()) {
        			if(c.getIdCourse()==cca.getPrimaryKey().getCourse_id() && c.getIdCourse()==id) {
        					teachedClasses.add(cca.getPrimaryKey().getClass_id());
        				}
        			}
        		}
        	
			List <Student> enrolledStud= studentDao.findAll();
			List<StudentResponse>listOfResp = new ArrayList<StudentResponse>();
			for (Student s:enrolledStud) {
				for(Integer idClass: teachedClasses) {
				if (s.getEnrolledClass().getIdClass()==idClass) {
					StudentResponse sr= new StudentResponse();
					sr.setRollNo(s.getRollNo());
					sr.setName(s.getName());
					sr.setLastName(s.getLastName());
					String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        		sr.addLink(uri, "self", "GET");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",idName).path("Classes")
	            			.path(Long.toString(id)).path("students")
	            			.path(Long.toString(sr.getRollNo()))
	            			.path("newMark")
	            			.build().toString();
	        		sr.addLink(uri, "give evaluation", "PUT");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",id).build().toString();
	            	sr.addLink(uri, "general services","GET");
	        		listOfResp.add(sr);
				}
				}
			}
        	System.out.println(listOfResp.size());
        	GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(listOfResp) {};
        	if (listOfResp.size()>0) {
                return Response.status(Response.Status.OK).entity(e).build();
            }
            else {
                return Response.status(Response.Status.BAD_REQUEST).entity("list empty").build();
            }
    }
//potrebbe mancare metodo di visione singolare dei corsi e/o degli studenti	
    @Path("Classes/{class_id}/timeTable")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getTimeTable(@PathParam("user_id") String id,@PathParam("class_id") int classId
    		,@Context UriInfo uriInfo) {
		List<TimeTable> tableList =timetableDao.findAll();
		List<TimeTableResponse> newList = new ArrayList<>();
		String uri;
		for (TimeTable tt : tableList) {
			if (tt.getIdTime().getCourseId()==classId) {
				TimeTableResponse newResp = new TimeTableResponse();
				newResp.setClassRoom(tt.getClassRoom());
				newResp.setCourseId(tt.getIdTime().getCourseId());
				newResp.setDay(tt.getIdTime().getDay());
				newResp.setStartingTime(tt.getIdTime().getStartingTime());
				newResp.setFinishTime(tt.getIdTime().getFinishTime());
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
				newResp.addLink(uri, "self", "GET");
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id", id).path("Classes")
						.path(Long.toString(classId)).path("students")
						.build().toString();
				newResp.addLink(uri, "students", "GET");
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	newResp.addLink(uri, "general services","GET");
				newList.add(newResp);
			}
		}
		GenericEntity<List<TimeTableResponse>> e = new GenericEntity<List<TimeTableResponse>>(newList) {};
		 if (newList.size()>0) {
	            return Response.status(Response.Status.OK).entity(e).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).entity("list empty").build();
	        }
	}
    
    @POST
	@Path("Classes/{course_id}/students/{stud_id}/newMark")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response setMarkPersonal(@PathParam("user_id") String teacherId,@PathParam("course_id") int idCourse,@PathParam("stud_id") int stud_id,
			EvaluationWrapper classIn,@Context UriInfo uriInfo) {
    	int check_courseFeasibility=0;
    	Teacher teacher = teacherDao.get(teacherId);
    	for(Course keepCourse: teacher.getCourseKeep()) {
    		if (classIn.getCw().getIdCourse()==keepCourse.getIdCourse() &&
    				keepCourse.getIdCourse()==idCourse) {
    			check_courseFeasibility=1;
    		} 
    	}
    	
    	Course course= courseDao.get(classIn.getCw().getIdCourse());
    	Student stud=studentDao.get(classIn.getSw().getRollNo());
    	if (course==null  || stud==null) {
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    	if (check_courseFeasibility==1) {
	    	List<CourseClassAssociation>allAssotiation=ccaDao.findAll();
	    	for(CourseClassAssociation cca : allAssotiation) {
	    		if(cca.getPrimaryKey().getClass_id()==stud.getEnrolledClass().getIdClass() &&
	    				cca.getPrimaryKey().getCourse_id()==idCourse) {
	    			Evaluation newEval= new Evaluation();
	    			newEval.setId(new Evaluation_Id());
	    			newEval.getId().setCourseId(course);
	    			newEval.getId().setStudentId(stud);
	    			newEval.setMark(classIn.getMark());
					if (newEval.getMark()>17) newEval.setPass(true);
					List<Link> uris = new ArrayList<Link>();
					String uri=uriInfo.getAbsolutePathBuilder().build().toString();
					addLinkToList(uris, uri, "self", "POST");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",teacherId).path("Classes")
	            			.path(Long.toString(idCourse)).path("students")
	            			.build().toString();
					addLinkToList(uris, uri, "see students", "GET");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
							.resolveTemplate("user_id",teacherId).build().toString();
			        addLinkToList(uris, uri, "general services", "GET");
					GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
					if (course_classDao.create(newEval)) {
			            return Response.status(Response.Status.OK).entity(e).build();
			        }
			        else {
			            return Response.status(Response.Status.BAD_REQUEST).build();
			        }
	    		}
	    	}
    	}
    	return Response.status(Response.Status.BAD_REQUEST).build();
		
	}
    
    
	@Path("appointments")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getAppointment(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
		
		List< AppointmentResponse> ds = new ArrayList<AppointmentResponse>();
		List<Appointment> apps = appointmentDao.findAll();
        for(Appointment a : apps) {
        	if(id.equals(a.getId().getTeacher().getTeacherId())) {
        		AppointmentResponse appResp = new AppointmentResponse();
        		appResp.setParentUsername(a.getId().getParents().getUsername());
        		appResp.setTeacherId(a.getId().getTeacher().getTeacherId());
        		appResp.setAppointmentDate(a.getId().getAppointmentDate());
        		String uri= uriInfo.getAbsolutePathBuilder().build().toString();
        		appResp.addLink(uri, "self", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class).resolveTemplate("user_id", id)
        				.path("setAppointments").path(appResp.getParentUsername()).build().toString();
        		appResp.addLink(uri, "modify appointment", "PUT");
        		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",id).build().toString();
        		appResp.addLink(uri, "new appointment", "POST");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        				.resolveTemplate("user_id",id).build().toString();
            	appResp.addLink(uri, "general services","GET");
        		ds.add(appResp);
        	}
        }
        GenericEntity<List<AppointmentResponse>> e = new GenericEntity<List<AppointmentResponse>>(ds) {};
        if (ds.size()>0) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).entity("list empty").build();
        }
        
    }
	
	//sistemaere struttura tabella appointment
	@PUT
	@Path("setAppointments/{parent_id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response  setAppointment(@PathParam("user_id") String id,@PathParam("parent_id")String parId,AppointmentWrapper upApp,
    		@Context UriInfo uriInfo) {
		if (id.equals(upApp.getTeacherId())&& parId.equals(upApp.getParentUsername())){
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
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id", id).path("appointments")
						.build().toString();
				addLinkToList(uris, uri, "see appointments", "GET");
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id",id).build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
				GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
				if(appointmentDao.create(newApp)) {
			    return Response.status(Response.Status.OK).entity(e).build();
			}else {
			    return Response.status(Response.Status.BAD_REQUEST).build();
			       }
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
        	if(id.equals(teacherId) && a.getId().getParents().getUsername().equals(parent)) {
        		if (appointmentDao.delete(a)) {
        			return a;}
        		else return null;
        	}
        }
		return null;
	}
	
	@Path("Notifications")
	@GET
	@Produces(MediaType.APPLICATION_XML)
   public Response  getPublicNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo) {
       	List<Notificatio>noteList = notificationDao.findAll();
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getUserName().equals(id)) {
       			NotificationResponse newNot= new NotificationResponse();
       			newNot.setNotificationNumber(n.getPrimaryKey().getNotificationNumber());
       			newNot.setUserName(n.getPrimaryKey().getUserName());
       			newNot.setSendDate(n.getSendDate());
       			newNot.setContentType(n.getContentType());
       			newNot.setContent(n.getContent());
       			String uri= uriInfo.getAbsolutePathBuilder().build().toString();
           		newNot.addLink(uri, "self", "GET");
           		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
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
}
