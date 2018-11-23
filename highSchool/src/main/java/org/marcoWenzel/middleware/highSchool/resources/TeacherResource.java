package org.marcoWenzel.middleware.highSchool.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.marcoWenzel.middleware.highSchool.dao.AbstractDAO;
import org.marcoWenzel.middleware.highSchool.dao.AppointmentDAO;
import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ClassDAO;
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.NotificationDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.dao.TimeTableDAO;
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.exception.NoContentException;
import org.marcoWenzel.middleware.highSchool.model.Appointment;
import org.marcoWenzel.middleware.highSchool.model.Classes;
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
import org.marcoWenzel.middleware.highSchool.response.ClassResponse;
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
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Path("Teacher/{user_id}")
public class TeacherResource {
	
	TeacherDAO teacherDao = new TeacherDAO();	
	AppointmentDAO appointmentDao = new AppointmentDAO();
	TimeTableDAO timetableDao = new TimeTableDAO();
	CourseDAO courseDao = new CourseDAO();
	EvaluationDAO course_classDao = new EvaluationDAO();
	CCAssotiationDAO ccaDao = new CCAssotiationDAO();
	StudentDAO studentDao=new StudentDAO();
	NotificationDAO notificationDao= new NotificationDAO();
	ClassDAO classDao = new ClassDAO();
	SimpleDateFormat timeTablesdf = new SimpleDateFormat("HH:mm:ss");
	TimeTableDAO timeTableDao = new TimeTableDAO();
	@GET

	public Response getTeacherServices(@PathParam("user_id")String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("personal")
				.build().toString();
		addLinkToList(uris, uri, "see personal", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("allClasses")
				.build().toString();
		addLinkToList(uris, uri, "see teached classes", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("allCourses")
				.build().toString();
		addLinkToList(uris, uri, "see teached courses", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id).path("allAppointments")
				.build().toString();
		addLinkToList(uris, uri, "see all appointments", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",id).path("allNotifications")
				.build().toString();
		addLinkToList(uris, uri, "see all notifications", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class)
				.resolveTemplate("user_id",id).path("newAppointment")
				.build().toString();
		addLinkToList(uris, uri, "new appointment", "POST");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	}
	@Path("personal")
	@GET

    public Response  getPersonalData(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
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
        			.resolveTemplate("user_id", id).path("allClasses").build().toString();
        	teacherResponse.addLink(uri, " see all teached classes", "GET");
        	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        			.resolveTemplate("user_id", id).path("allCourses").build().toString();
        	teacherResponse.addLink(uri, " see all teached courses", "GET");
        	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
    				.resolveTemplate("user_id",id).build().toString();
        	teacherResponse.addLink(uri, "general services","GET");
        	if (thisTeacher.getTeacherId().equals(id)) {
	            return Response.status(Response.Status.OK).entity(teacherResponse).type(negotiation(h)).build();
	        }
	        else {
	        	 throw new DataNotFoundException();
	        	 }
        	
    }
	
// vedere cosa cambia per triggerare altre query
	@PUT
	@Path("personal")
	public Response setTeacherPersonal(TeacherWrapper teaIn,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
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
	            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	        }
	    else {
	        	 throw new DataNotFoundException();
	        	 }
	}

	@GET
	@Path("allCourses")
    public Response  getCourses(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<CourseResponse> teachedCourses = new ArrayList<CourseResponse>();
        	Teacher thisTeacher = teacherDao.get(id);
        	if (!thisTeacher.getTeacherId().equals(id)) 
        		throw new DataNotFoundException();
        	Iterator<Course> onCourse =thisTeacher.getCourseKeep().iterator();
        	while (onCourse.hasNext()) {
        		Course course = onCourse.next();
        		CourseResponse newCourse = new CourseResponse();
        		newCourse.setClassRoom(course.getClassRoom());
        		newCourse.setCourseDescription(course.getCourseDescription());
        		newCourse.setCourseName(course.getCourseName());
        		newCourse.setIdCourse(course.getIdCourse());
        		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
        		newCourse.addLink(uri, "self", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
            			.resolveTemplate("user_id", id).path("Course")
            			.path(Long.toString(newCourse.getIdCourse()))
            			.build().toString();
        		newCourse.addLink(uri, "see specific course", "GET");
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        				.resolveTemplate("user_id",id).build().toString();
            	newCourse.addLink(uri, "general services","GET");
        		teachedCourses.add(newCourse);
        	}
        	GenericEntity<List<CourseResponse>> e = new GenericEntity<List<CourseResponse>>(teachedCourses) {};
        	if (!teachedCourses.isEmpty()) {
                return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
            }
            else {
            	throw new NoContentException();
            	}
    }
    @Path("allClasses")
	@GET
	
	public Response  getAllteachedClass(@PathParam("user_id") String idName,@Context UriInfo uriInfo,@Context HttpHeaders h) {
    	Teacher teacher =teacherDao.get(idName);
    	if (teacher==null)
    		throw new DataNotFoundException();
    	List <CourseClassAssociation> listCca= ccaDao.findAll();
    	List<Integer> listOfCourses = new ArrayList<Integer>();
    	List <ClassResponse> listOfClasses = new ArrayList<ClassResponse>();
    	for (Course c : teacher.getCourseKeep())
    		listOfCourses.add(c.getIdCourse());
		
		for(CourseClassAssociation cca : listCca) {
			if (listOfCourses.contains(cca.getPrimaryKey().getCourse_id())) {
				Classes cl= classDao.get(cca.getPrimaryKey().getClass_id());
				ClassResponse cr = new ClassResponse();
				cr.setClassName(cl.getClassName());
				cr.setIdClass(cl.getIdClass());
				String uri;
				if (listOfClasses.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        		cr.addLink(uri, "self", "GET");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",idName).build().toString();
	            	cr.addLink(uri, "general services","GET");
				}
				
				uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
        				.resolveTemplate("user_id",idName)
        				.path("Class").path(String.valueOf(cr.getIdClass()))
        				.build().toString();
				cr.addLink(uri, "see this specific class","GET");
				listOfClasses.add(cr);
			}
		}
		GenericEntity<List<ClassResponse>> e = new GenericEntity<List<ClassResponse>>(listOfClasses) {};
    	if (!listOfClasses.isEmpty()) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}		
    	
    }
    @Path("Class/{class_id}")
    @GET
    public Response getSpecificClass(@PathParam("user_id") String idName,@PathParam("class_id") int classId,@Context UriInfo uriInfo,@Context HttpHeaders h) {
    	Teacher teacher =teacherDao.get(idName);
    	if (teacher==null)
    		throw new DataNotFoundException();
    	
    	List<Integer> listOfCourses = new ArrayList<Integer>();
    	
    	for (Course c : teacher.getCourseKeep())
    		listOfCourses.add(c.getIdCourse());
    	if (teacherKeep(listOfCourses, classId)==false)
    		throw new DataNotFoundException();
    	Classes specifiClass= classDao.get(classId);
		if (specifiClass==null)
			throw new DataNotFoundException();
		ClassResponse cresp= new ClassResponse();
		cresp.setIdClass(specifiClass.getIdClass());
		cresp.setClassName(specifiClass.getClassName());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		cresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).build().toString();
    	cresp.addLink(uri, "general services","GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).path("allClasses")	
				.build().toString();
		cresp.addLink(uri, "see all classes", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).path("Class")
				.path(String.valueOf(classId)).path("students")
				.build().toString();
		cresp.addLink(uri, "see students of this class", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).path("TimeTable")
    			.path(String.valueOf(classId))
				.build().toString();
    	cresp.addLink(uri, "see the timeTable class","GET");
		return Response.status(Response.Status.OK).entity(cresp).type(negotiation(h)).build();
    	
    }
    public boolean teacherKeep(List<Integer> courseKeep,int classId) {
    	List <CourseClassAssociation> listCca= ccaDao.findAll();
    	for(CourseClassAssociation cca : listCca) {
    		if (courseKeep.contains(cca.getPrimaryKey().getCourse_id())  && classId== cca.getPrimaryKey().getClass_id()) 
    			return true;
    	}
    	return false;
    }
	@Path("Class/{class_id}/students")
	@GET
    public Response  getAllStudInClass(@PathParam("user_id") String idName,@PathParam("class_id") int id,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Teacher teacher = teacherDao.get(idName);
		if (teacher==null)
    		throw new DataNotFoundException();
    	
    	List<Integer> listOfCourses = new ArrayList<Integer>();
    	
    	for (Course c : teacher.getCourseKeep())
    		listOfCourses.add(c.getIdCourse());
    	if (teacherKeep(listOfCourses, id)==false)
    		throw new DataNotFoundException();
	
		List <Student> enrolledStud= studentDao.findAll();
		List<StudentResponse>listOfResp = new ArrayList<StudentResponse>();
		for (Student s:enrolledStud) {
			if (s.getEnrolledClass()!=null && s.getEnrolledClass().getIdClass()==id) {
				StudentResponse sr= new StudentResponse();
				sr.setRollNo(s.getRollNo());
				sr.setName(s.getName());
				sr.setLastName(s.getLastName());
				String uri;
				if (listOfResp.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        		sr.addLink(uri, "self", "GET");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",idName).build().toString();
	            	sr.addLink(uri, "general services","GET");
	            	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",idName)
	        				.path("Class").path(String.valueOf(s.getEnrolledClass().getIdClass()))
	        				.build().toString();
					sr.addLink(uri, "see this specific class","GET");
				}
				
        		listOfResp.add(sr);
			}
			
		}
    	GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(listOfResp) {};
    	if (listOfResp.size()>0) {
            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
        }
        else {
        	throw new NoContentException();
        	}
    }
	
	@Path("Course/{course_id}")
	@GET
	public Response  getCourseInSpecific(@PathParam("user_id") String idName,@PathParam("course_id") int id,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Teacher teacher = teacherDao.get(idName);
		if (teacher==null)
    		throw new DataNotFoundException();
    	
    	List<Integer> listOfCourses = new ArrayList<Integer>();
    	
    	for (Course c : teacher.getCourseKeep())
    		listOfCourses.add(c.getIdCourse());
    	
    	if (!listOfCourses.contains(id)) 
    		throw new DataNotFoundException();
    	Course course = courseDao.get(id);
    	CourseResponse cresp= new CourseResponse();
		cresp.setIdCourse(course.getIdCourse());
		cresp.setClassRoom(course.getClassRoom());
		cresp.setCourseName(course.getCourseName());
		cresp.setCourseDescription(course.getCourseDescription());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		cresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        cresp.addLink( uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).path("Course")
				.path(String.valueOf(course.getIdCourse())).path("students")
				.build().toString();
		cresp.addLink(uri, "see students of this course", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",idName).path("allCourses")
				.build().toString();
		cresp.addLink(uri, "see students of this course", "GET");
       
        return Response.status(Response.Status.OK).entity(cresp).type(negotiation(h)).build();
	}
    	
    	
	
	
	
	@Path("Course/{course_id}/students")
	@GET
    public Response  getAllStudInCourse(@PathParam("user_id") String idName,@PathParam("course_id") int id,
    		@Context UriInfo uriInfo,@Context HttpHeaders h) {
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
				if (s.getEnrolledClass()!=null && s.getEnrolledClass().getIdClass()==idClass) {
					StudentResponse sr= new StudentResponse();
					sr.setRollNo(s.getRollNo());
					sr.setName(s.getName());
					sr.setLastName(s.getLastName());
					String uri;
					if (listOfResp.isEmpty()) {
						uri=uriInfo.getAbsolutePathBuilder().build().toString();
		        		sr.addLink(uri, "self", "GET");
		        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
		        				.resolveTemplate("user_id",id).build().toString();
		            	sr.addLink(uri, "general services","GET");
		            	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
		        				.resolveTemplate("user_id",id)
		        				.path("Course").path(String.valueOf(id))
		        				.build().toString();
		            	sr.addLink(uri, "see the specific course","GET");
					}
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",idName).path("Classes")
	            			.path(Long.toString(id)).path("students")
	            			.path(Long.toString(sr.getRollNo()))
	            			.path("Mark")
	            			.build().toString();
	        		sr.addLink(uri, "give evaluation", "PUT");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",idName).path("Classes")
	            			.path(Long.toString(id)).path("students")
	            			.path(Long.toString(sr.getRollNo()))
	            			.path("Mark")
	            			.build().toString();
	        		sr.addLink(uri, "see evaluation", "GET");
	        		listOfResp.add(sr);
				}
				}
			}
        	GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(listOfResp) {};
        	if (listOfResp.size()>0) {
                return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
            }
            else {
            	throw new NoContentException();
            	}
    }
	@Path("TimeTable/{class_id}")
	@GET
	 public Response seeClassTimeTable(@PathParam("class_id")int id ,@PathParam("user_id")String teacherId,
	    		@Context UriInfo uriInfo,@Context HttpHeaders h) throws ParseException {
		Classes classTT= classDao.get(id);
		if(classTT==null)
			throw new DataNotFoundException();
		List <CourseClassAssociation> listCca= ccaDao.findAll();
		List<Integer> listOfCourses = new ArrayList<Integer>();
		for(CourseClassAssociation cca : listCca) {
			if (classTT.getIdClass()==cca.getPrimaryKey().getClass_id()) 
				listOfCourses.add(cca.getPrimaryKey().getCourse_id());	
		}
		System.out.println("loc: "+listOfCourses);
		List<TimeTableResponse> ttList= new ArrayList<TimeTableResponse>();
		List<TimeTable> timeTableList=timeTableDao.findAll();
		String uri;
		for(TimeTable tt: timeTableList) {
			if (listOfCourses.contains(tt.getIdTime().getCourseId())) {
				TimeTableResponse ttresp= new TimeTableResponse();
				ttresp.setClassRoom(tt.getClassRoom());
				ttresp.setCourseId(tt.getIdTime().getCourseId());
				ttresp.setDay(tt.getIdTime().getDay());
				String c=timeTablesdf.format(tt.getIdTime().getStartingTime());
				ttresp.setStartingTime(c);
				c=timeTablesdf.format(tt.getIdTime().getFinishTime());	
				ttresp.setFinishTime(c);
				
				if (ttList.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
	        		ttresp.addLink(uri, "self", "GET");
	        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",teacherId).build().toString();
	            	ttresp.addLink(uri, "general services","GET");
					
				}
				ttList.add(ttresp);
			}
		}
		GenericEntity<List<TimeTableResponse>> e = new GenericEntity<List<TimeTableResponse>>(ttList) {};
		if (!ttList.isEmpty())
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		else
			throw new NoContentException();
	}
	
	@GET
	@Path("Course/{course_id}/students/{stud_id}/Mark")
	public Response setMarkPersonal(@PathParam("user_id") String teacherId,@PathParam("course_id") int idCourse,@PathParam("stud_id") int stud_id,
			@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Evaluation> evalList = course_classDao.findAll();
		List<EvaluationResponse> evalRespList = new ArrayList<EvaluationResponse>();
		for (Evaluation e : evalList) {
			if (e.getId().getCourseId().getIdCourse()==idCourse && e.getId().getStudentId().getRollNo()==stud_id) {
				EvaluationResponse newResp= new EvaluationResponse();
				newResp.setCourseId(e.getId().getCourseId().getIdCourse());
				newResp.setSonId(e.getId().getStudentId().getRollNo());
				newResp.setMark(e.getMark());
				String uri;
				if (evalRespList.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
					newResp.addLink(uri, "self", "GET");
					uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
							.build().toString();
					newResp.addLink(uri, "general services", "GET");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",teacherId).path("Courses")
	            			.path(Long.toString(idCourse)).path("students")
	            			.build().toString();
					newResp.addLink( uri, "see students", "GET");
				}
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
				newResp.addLink(uri, "add new evaluation for this student", "POST");
				evalRespList.add(newResp);
			}
		}
		GenericEntity<List<EvaluationResponse>> e = new GenericEntity<List<EvaluationResponse>>(evalRespList) {};
		if (!evalRespList.isEmpty())
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		else
			throw new NoContentException();
	}
    @POST
	@Path("Course/{course_id}/students/{stud_id}/Mark")

	public Response setMarkPersonal(@PathParam("user_id") String teacherId,@PathParam("course_id") int idCourse,@PathParam("stud_id") int stud_id,
			EvaluationWrapper classIn,@Context UriInfo uriInfo,@Context HttpHeaders h) {
    	int check_courseFeasibility=0;

    	if (idCourse!= classIn.getCourseId() || classIn.getSonId()!=stud_id)
    		throw new DataNotFoundException();
    	System.out.println("passo");
    	Teacher teacher = teacherDao.get(teacherId);
    	for(Course keepCourse: teacher.getCourseKeep()) {
    		if (idCourse==keepCourse.getIdCourse()) {
    			check_courseFeasibility=1;
    		} 
    	}
    	
    	Course course= courseDao.get(idCourse);
    	Student stud=studentDao.get(stud_id);
    	if (course==null  || stud==null) {
    		 throw new DataNotFoundException();
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
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
					addLinkToList(uris, uri, "see evalutations", "GET");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	            			.resolveTemplate("user_id",teacherId).path("Courses")
	            			.path(Long.toString(idCourse)).path("students")
	            			.build().toString();
					addLinkToList(uris, uri, "see students", "GET");
					uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
							.resolveTemplate("user_id",teacherId).build().toString();
			        addLinkToList(uris, uri, "general services", "GET");
					GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
					if (course_classDao.create(newEval)) {
			            return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
			        }
			        else {
			        	 throw new DataNotFoundException();
			        	 }
	    		}
	    	}
    	}
    	 throw new DataNotFoundException();
    	 
	}
    
  /*
	@Path("allAppointments")
	@GET
    public Response  getAppointment(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
		List< AppointmentResponse> ds = new ArrayList<AppointmentResponse>();
		List<Appointment> apps = appointmentDao.findAll();
        for(Appointment a : apps) {
        	if(id.equals(a.getTeacherId())) {
        		AppointmentResponse appResp = new AppointmentResponse();
        		appResp.setAppointmentId(a.getAppointId());
        		appResp.setParentUsername(a.getParentId());
        		appResp.setTeacherId(a.getTeacherId());
        		appResp.setAppointmentDate(a.getAppointmentDate());
        		String uri;
        		if(ds.isEmpty()) {
	        		uri= uriInfo.getAbsolutePathBuilder().build().toString();
	        		appResp.addLink(uri, "self", "GET");
	           		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	        				.resolveTemplate("user_id",id).build().toString();
	            	appResp.addLink(uri, "general services","GET");
        		}
        		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class).resolveTemplate("user_id",id)
        				.path("Appointment").path(String.valueOf(appResp.getAppointmentId()))
        				.build().toString();
        		appResp.addLink(uri, "see specific appointment","GET");
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
	@GET
	@Path("Appointment/{appoint_id}")
	 public Response  getAppointment(@PathParam("user_id") String teacherid,@PathParam("appoint_id") int AppId,
	    	@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Appointment app= appointmentDao.get(AppId);
		if (app==null)
			throw new DataNotFoundException();
		if (!app.getTeacherId().equals(teacherid))
			throw new DataNotFoundException();
	
		AppointmentResponse aResp= new AppointmentResponse();
		aResp.setTeacherId(teacherid);
		aResp.setParentUsername(app.getParentId());
		aResp.setAppointmentDate(app.getAppointmentDate());
		aResp.setAppointmentId(app.getAppointId());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		aResp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",teacherid).build().toString();
    	aResp.addLink(uri, "general services","GET");
    	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
				.resolveTemplate("user_id",teacherid)
				.path("allAppointments")
				.build().toString();
    	aResp.addLink(uri, "all appointments","GET");
        uri=uriInfo.getAbsolutePathBuilder().build().toString();
        aResp.addLink( uri, "modify this appoinment", "PUT");
        uri=uriInfo.getBaseUriBuilder().path(AppointmentResource.class).resolveTemplate("user_id",teacherid).build().toString();
		aResp.addLink(uri, "new appointment", "POST");
        uri=uriInfo.getAbsolutePathBuilder().build().toString();
		aResp.addLink(uri, "delete appointment", "DELETE");
		return Response.status(Response.Status.OK).entity(aResp).type(negotiation(h)).build();
		
	}
	
	@PUT
	@Path("Appointment/{appoint_id}")

    public Response  setAppointment(@PathParam("user_id") String teacherid,@PathParam("appoint_id") int AppId,
    		AppointmentWrapper upApp,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Appointment oldApp= appointmentDao.get(AppId);
		if(oldApp==null)
			 throw new DataNotFoundException();
		if(upApp.getParentUsername().equals(oldApp.getParentId()) && upApp.getTeacherId().equals(oldApp.getTeacherId())
				&& upApp.getTeacherId().equals(teacherid)) {
			oldApp.setAppointmentDate(upApp.getAppointmentDate());
			List<Link> uris = new ArrayList<Link>();
			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
			addLinkToList(uris, uri, "self", "PUT");
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id", teacherid).path("allAppointments")
					.build().toString();
			addLinkToList(uris, uri, "see  all appointments", "GET");
			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
					.resolveTemplate("user_id",teacherid).build().toString();
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
	@Path("Appointment/{appoint_id}")
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
					.resolveTemplate("user_id", teacherid).path("allAppointments")
					.build().toString();
			addLinkToList(uris, uri, "see all appointments", "GET");
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
	@Path("allNotifications")
	@GET

   public Response  getPublicNotif(@PathParam("user_id") String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
       	List<Notificatio>noteList = notificationDao.findAll();
       	if (noteList==null)
       	 throw new NoContentException();
       	List<NotificationResponse> newList = new ArrayList<NotificationResponse>();
       	for(Notificatio n : noteList) {
       		if(n.getPrimaryKey().getReceiver().equals(id)) {
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
	           		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
		    				.resolveTemplate("user_id",id).build().toString();
		        	newNot.addLink(uri, "general services","GET");
       			}
       			uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("Notification").path(String.valueOf(newNot.getNotificationNumber()))
	    				.build().toString();
           		newNot.addLink(uri, "see specific notification", "GET");
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
           		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	    				.resolveTemplate("user_id",id).build().toString();
	        	newNot.addLink(uri, "general services","GET");
	        	uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
	    				.resolveTemplate("user_id",id)
	    				.path("allNotifications")
	    				.build().toString();
	        	newNot.addLink(uri, "see all notifications","GET");
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
