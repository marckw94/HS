package org.marcoWenzel.middleware.highSchool.resources;

import java.util.ArrayList;
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
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.Course_ClassDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.dao.TimeTableDAO;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Appointment_Id;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.Course_Class;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Teacher;
import org.marcoWenzel.middleware.highSchool.model.TimeTable;
import org.marcoWenzel.middleware.highSchool.response.AppointmentResponse;
import org.marcoWenzel.middleware.highSchool.response.CourseResponse;
import org.marcoWenzel.middleware.highSchool.response.Course_ClassResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.response.TeacherResponse;
import org.marcoWenzel.middleware.highSchool.response.TimeTableResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.AppointmentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.CourseWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Course_ClassWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.ParentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.TeacherWrapper;
@Secured({Category.Teacher})
@Path("teacher/{user_id}")
public class TeacherResource {
	
	TeacherDAO teacherDao = new TeacherDAO();	
	AppointmentDAO appointmentDao = new AppointmentDAO();
	TimeTableDAO timetableDao = new TimeTableDAO();
	CourseDAO courseDao = new CourseDAO();
	Course_ClassDAO course_classDao = new Course_ClassDAO();
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getParentServices(@PathParam("user_id")String id,@Context UriInfo uriInfo) {
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
	@Path("Classes")
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

	@Path("Classes/{class_id}/students")
	@GET
	@Produces(MediaType.APPLICATION_XML)
    public Response  getAllStudInClass(@PathParam("user_id") String idName,@PathParam("class_id") int id,@Context UriInfo uriInfo) {
        	List<Course_Class> allCourse = course_classDao.findAll();
        	Course_ClassResponse newResp = new Course_ClassResponse();
        	List<Course_ClassResponse> listOfResp = new ArrayList<Course_ClassResponse>();
        	for(Course_Class cc : allCourse) {
        		int idcc= cc.getId().getCourseId().getIdCourse();
        		if(idcc ==id) {
        			newResp = new Course_ClassResponse();
	        		newResp.getCw().setCourseName(cc.getId().getCourseId().getCourseName());
	        		newResp.getCw().setIdCourse(cc.getId().getCourseId().getIdCourse());
	        		newResp.getSw().setLastName(cc.getId().getStudentId().getLastName());
	        		newResp.getSw().setRollNo(cc.getId().getStudentId().getRollNo());
	        		newResp.setMark(cc.getMark());
	        		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        		newResp.addLink(uri, "self", "GET");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",idName).path("Classes")
	            			.path(Long.toString(id)).path("students")
	            			.path(Long.toString(newResp.getSw().getRollNo()))
	            			.path("newMark")
	            			.build().toString();
	        		newResp.addLink(uri, "give evaluation", "PUT");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",id).build().toString();
	            	newResp.addLink(uri, "general services","GET");
	        		listOfResp.add(newResp);
        		}
        	}
        	System.out.println(listOfResp.size());
        	GenericEntity<List<Course_ClassResponse>> e = new GenericEntity<List<Course_ClassResponse>>(listOfResp) {};
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
    
    @PUT
	@Path("Classes/{class_id}/students/{stud_id}/newMark")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response setMarkPersonal(@PathParam("user_id") String teacherId,@PathParam("stud_id") int stud_id,
			Course_ClassWrapper classIn,@Context UriInfo uriInfo) {
		int courseId=classIn.getCw().getIdCourse();
		int markedSonId=classIn.getSw().getRollNo();
		int check_courseFeasibility=0;
		Teacher teacher = teacherDao.get(teacherId);
		for (Course c : teacher.getCourseKeep()) {
			if (courseId==c.getIdCourse()) {
				check_courseFeasibility=1;
			}
		}
		if (check_courseFeasibility==1) {
			List <Course_Class> courses = course_classDao.findAll();
			for (Course_Class cc : courses) {
				System.out.println(cc.getId().getCourseId().getIdCourse());
				System.out.println(cc.getId().getStudentId().getRollNo());
				if (cc.getId().getCourseId().getIdCourse()==courseId && 
						cc.getId().getStudentId().getRollNo()==markedSonId &&
						stud_id==markedSonId) {
					cc.setMark(classIn.getMark());
					if (cc.getMark()>17) cc.setPass(true);
					List<Link> uris = new ArrayList<Link>();
					String uri=uriInfo.getAbsolutePathBuilder().build().toString();
					addLinkToList(uris, uri, "self", "PUT");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",teacherId).path("Classes")
	            			.path(Long.toString(courseId)).path("students")
	            			.build().toString();
					addLinkToList(uris, uri, "see students", "GET");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
							.resolveTemplate("user_id",teacherId).build().toString();
			        addLinkToList(uris, uri, "general services", "GET");
					GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
					if (course_classDao.update(cc)) {
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
	
	public void addLinkToList(List<Link> list,String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		list.add(newL);
	}
}
