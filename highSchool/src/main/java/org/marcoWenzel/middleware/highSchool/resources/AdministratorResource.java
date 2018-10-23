package org.marcoWenzel.middleware.highSchool.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
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

import org.marcoWenzel.middleware.highSchool.dao.AdministratorDAO;
import org.marcoWenzel.middleware.highSchool.dao.CCAssotiationDAO;
import org.marcoWenzel.middleware.highSchool.dao.ClassDAO;
import org.marcoWenzel.middleware.highSchool.dao.CourseDAO;
import org.marcoWenzel.middleware.highSchool.dao.EvaluationDAO;
import org.marcoWenzel.middleware.highSchool.dao.LogInDAO;
import org.marcoWenzel.middleware.highSchool.dao.ParentDAO;
import org.marcoWenzel.middleware.highSchool.dao.PaymentDAO;
import org.marcoWenzel.middleware.highSchool.dao.StudentDAO;
import org.marcoWenzel.middleware.highSchool.dao.TeacherDAO;
import org.marcoWenzel.middleware.highSchool.dao.TimeTableDAO;
import org.marcoWenzel.middleware.highSchool.model.Administrator;
import org.marcoWenzel.middleware.highSchool.model.Classes;
import org.marcoWenzel.middleware.highSchool.model.Course;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation;
import org.marcoWenzel.middleware.highSchool.model.CourseClassAssociation_Id;
import org.marcoWenzel.middleware.highSchool.model.Evaluation;
import org.marcoWenzel.middleware.highSchool.model.Evaluation_Id;
import org.marcoWenzel.middleware.highSchool.model.LogIn;
import org.marcoWenzel.middleware.highSchool.model.Parent;
import org.marcoWenzel.middleware.highSchool.model.Payement;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.model.Teacher;
import org.marcoWenzel.middleware.highSchool.model.TimeTable;
import org.marcoWenzel.middleware.highSchool.model.TimeTable_Id;
import org.marcoWenzel.middleware.highSchool.response.ClassResponse;
import org.marcoWenzel.middleware.highSchool.response.CourseResponse;
import org.marcoWenzel.middleware.highSchool.response.ParentResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.wrapper.ClassWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.CourseWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.EvaluationWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.EnrollRequest;
import org.marcoWenzel.middleware.highSchool.wrapper.ParentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.PaymentWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.TeacherWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.TimeTableWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Wrapper;
//@Secured({Category.Admin})
@Path("Admin")
public class AdministratorResource {
	AdministratorDAO administratorDao = new AdministratorDAO();
	TimeTableDAO timeTableDao = new TimeTableDAO();
	CourseDAO courseDao = new CourseDAO();
	LogInDAO loginDao = new LogInDAO();
	StudentDAO studentDao = new StudentDAO();
	ParentDAO parentDao = new ParentDAO();
	EvaluationDAO course_classDao = new EvaluationDAO();
	TeacherDAO teacherDao = new TeacherDAO();
	PaymentDAO paymentDao = new PaymentDAO();
	ClassDAO classDao = new ClassDAO();
	CCAssotiationDAO ccaDao = new CCAssotiationDAO();
	@GET 
	@Path("allCourses")
	@Produces(MediaType.APPLICATION_XML)
	public Response getAllClasses(@Context UriInfo uriInfo) {
		List<Course>allCourses=courseDao.findAll();
		List <CourseResponse> crs= new ArrayList<CourseResponse>();
		for (Course i : allCourses) {
    		CourseResponse newClass = new CourseResponse();
    		newClass.setClassRoom(i.getClassRoom());
    		newClass.setCourseDescription(i.getCourseDescription());
    		newClass.setCourseName(i.getCourseName());
    		newClass.setIdCourse(i.getIdCourse());
    		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
    		newClass.addLink(uri, "self", "GET");
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
    				.build().toString();
            newClass.addLink( uri, "general services", "GET");
    		crs.add(newClass);
		}
		GenericEntity<List<CourseResponse>> e = new GenericEntity<List<CourseResponse>>(crs) {};
		if(allCourses.size()>0) {
			return Response.status(Response.Status.OK).entity(e).build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET 
	@Path("allParents")
	@Produces(MediaType.APPLICATION_XML)
	public Response getAllParents(@Context UriInfo uriInfo) {
		List<Parent>allParents=parentDao.findAll();
		List <ParentResponse> crs= new ArrayList<ParentResponse>();
		for (Parent i : allParents) {
    		ParentResponse newPar = new ParentResponse();
    		newPar.setUsername(i.getUsername());
    		newPar.setPassword("*****");
    		newPar.setName(i.getName());
    		newPar.setSurname(i.getSurname());
    		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
    		newPar.addLink(uri, "self", "GET");
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
    				.build().toString();
            newPar.addLink( uri, "general services", "GET");
    		crs.add(newPar);
		}
		GenericEntity<List<ParentResponse>> e = new GenericEntity<List<ParentResponse>>(crs) {};
		if(allParents.size()>0) {
			return Response.status(Response.Status.OK).entity(e).build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET 
	@Path("allStudents")
	@Produces(MediaType.APPLICATION_XML)
	public Response getAllStudents(@Context UriInfo uriInfo) {
		List<Student>allStudents=studentDao.findAll();
		List <StudentResponse> crs= new ArrayList<StudentResponse>();
		for (Student i : allStudents) {
    		StudentResponse newStud = new StudentResponse();
    		newStud.setRollNo(i.getRollNo());
    		newStud.setLastName(i.getLastName());
    		newStud.setName(i.getName());
    		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
    		newStud.addLink(uri, "self", "GET");
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
    				.build().toString();
            newStud.addLink( uri, "general services", "GET");
    		crs.add(newStud);
		}
		GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(crs) {};
		if(allStudents.size()>0) {
			return Response.status(Response.Status.OK).entity(e).build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getParentServices(@PathParam("user_id")String id,@Context UriInfo uriInfo) {
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("enrollment")
				.build().toString();
		addLinkToList(uris, uri, "enroll student", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("Classes").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "see all the students of a specific class", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newAdministrator")
				.build().toString();
		addLinkToList(uris, uri, "create new admin", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("createParent")
				.build().toString();
		addLinkToList(uris, uri, "create new Parent", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("createTeacher")
				.build().toString();
		addLinkToList(uris, uri, "create new Teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newCourse")
				.build().toString();
		addLinkToList(uris, uri, "create new course", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("association").path("[teacher_id]").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "assign a class to a teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newPayment")
				.build().toString();
		addLinkToList(uris, uri, "issue new payment", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newCourse")
				.build().toString();
		addLinkToList(uris, uri, "create new course", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("newParentNotif").path("[user_id]")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to a single parent", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("newTeacherNotif").path("[user_id]")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to a single teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("allPar")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all parents", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("allTeach")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all teachers", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("ClassPar").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all the parent of a specific class ", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("ClassTeach").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to the teacher of a specific class ", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("timeTable").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "add new class hours", "POST");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).build();
	}
	@PUT
	@Path("enrollment")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response enrollStud(EnrollRequest newEnroll,@Context UriInfo uriInfo) {
		Student student = studentDao.get(newEnroll.getIdStud());
		
		if (student.getEnrolledClass()!=null) {
			 return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Classes enrollClass =classDao.get(newEnroll.getIdClass());
		enrollClass.setEnrolledStud(new ArrayList<Student>());
		enrollClass.getEnrolledStud().add(student);
		student.setEnrolledClass(enrollClass);
		/*Course_Class enroll = new Course_Class();
		Student student = studentDao.get(newEnroll.getSw().getRollNo());
		Course course = courseDao.get(newEnroll.getCw().getIdCourse());
		enroll.setId(new Course_Class_Id());
		enroll.getId().setCourseId(course);
		enroll.getId().setStudentId(student);
		enroll.setMark(0);
		enroll.setPass(false);*/
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		/*uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("classes")
				.path(Long.toString(enroll.getId().getCourseId().getIdCourse()))
				.build().toString();
		addLinkToList(uris, uri, "see other subscirbed students", "GET");*/
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (classDao.update(enrollClass) && studentDao.update(student)) {
	            return Response.status(Response.Status.OK).entity(e).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	}
	
	@GET
	@Path("classes/{class_id}")
	@Produces(MediaType.APPLICATION_XML)
	 public Response allStudEnrolled(@PathParam("class_id") int idClass,@Context UriInfo uriInfo) {
		List<Classes> ccList = classDao.findAll();
		List<StudentResponse> studList = new ArrayList<StudentResponse>();
		if (ccList == null )
			 return Response.status(Response.Status.BAD_REQUEST).build();
		else 
			for (Classes cc : ccList) {
				if (cc.getIdClass()==idClass) {
					for (Student s : cc.getEnrolledStud()) {
						StudentResponse newStud= new StudentResponse();
						newStud.setRollNo(s.getRollNo());
						newStud.setLastName(s.getLastName());
						newStud.setName(s.getName());
						String uri= uriInfo.getAbsolutePathBuilder().build().toString();
						newStud.addLink(uri, "self", "GET");
						uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
			    				.build().toString();
			        	newStud.addLink(uri, "general services","GET");
						studList.add(newStud);
					}	
				}
			}
		GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(studList) {};
		if(studList.size()>0) {
			return Response.status(Response.Status.OK).entity(e).build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	//serve una sorta schermata di home
	@Path("newAdministrator")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response createAdmin(Administrator newA,@Context UriInfo uriInfo) {
		LogIn newLog= new LogIn();
		newLog.setCategory("Admin");
		newLog.setPassword(newA.getPassword());
		newLog.setUsername(newA.getUsername());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (loginDao.create(newLog) && administratorDao.create(newA)) {
	            return Response.status(Response.Status.OK).entity(e).build();
	        }
	        else {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	
	}
	
	@Path("createParent")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createParent(ParentWrapper newP,@Context UriInfo  uriInfo) {
		LogIn newlog= new LogIn();
		Parent newParent = new Parent();
		newParent.setName(newP.getName());
		newParent.setUsername(newP.getUsername());
		newParent.setPassword(newP.getPassword());
		newParent.setSurname(newP.getSurname());
		newlog.setUsername(newP.getUsername());
		newlog.setCategory("Parent");
		newlog.setPassword(newP.getPassword());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path(newParent.getUsername()).path("newSon").build().toString();
		addLinkToList(uris, uri, "add son", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newlog!= null && loginDao.create(newlog) && newParent!=null && parentDao.create(newParent)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@Path("{parent_id}/newSon")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createSon(Wrapper newS,@PathParam("parent_id") String parentS,@Context UriInfo uriInfo) {
		Parent parent = parentDao.get(parentS);
		Student newStud = new Student();
		newStud.setName(newS.getName());
		newStud.setLastName(newS.getLastName());
		newStud.setParentUsername(parent);
		newStud.setRollNo(newS.getRollNo());
		parent.getSon().add(newStud);
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("enrollment").build().toString();
		addLinkToList(uris, uri, "enroll student to a corse", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		if(newStud!= null && studentDao.create(newStud) && parentDao.update(parent)) {
			return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();	
	}
	
	
	@Path("createTeacher")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createTeacher(TeacherWrapper newT,@Context UriInfo uriInfo) {
		List<Course> courseList=courseDao.findAll();
		LogIn newlog= new LogIn();
		newlog.setUsername(newT.getTeacherId());
		newlog.setCategory("Teacher");
		newlog.setPassword(newT.getPassword());
		Teacher newTc = new Teacher();
		newTc.setName(newT.getName());
		newTc.setPassword(newT.getPassword());
		newTc.setTeacherId(newT.getTeacherId());
		newTc.setSurname(newT.getSurname());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		for(Course c : courseList) {
			if (c.getTeacher()==null) {
				uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
						.path("association").path(newTc.getTeacherId())
						.path(Long.toString(c.getIdCourse())).build().toString();
				addLinkToList(uris, uri, "assign to this course", "PUT");
				uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
						.build().toString();
		        addLinkToList(uris, uri, "general services", "GET");
			}
		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newlog!= null && loginDao.create(newlog) && newTc!=null && teacherDao.create(newTc)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@Path("newCourse")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createCourse(CourseWrapper newC,@Context UriInfo uriInfo) {
		List<Teacher> teacherList = teacherDao.findAll();
		Course newCourse= new Course();
		newCourse.setClassRoom(newC.getClassRoom());
		newCourse.setCourseDescription(newC.getCourseDescription());
		newCourse.setCourseName(newC.getCourseName());
		newCourse.setIdCourse(newC.getIdCourse());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		for(Teacher t : teacherList) {
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("association")
					.path(t.getTeacherId()).path(Long.toString(newCourse.getIdCourse()))
					.build().toString();
			addLinkToList(uris, uri, "assign to a teacher", "PUT");
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.build().toString();
	        addLinkToList(uris, uri, "general services", "GET");
		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newCourse!= null && courseDao.create(newCourse)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	@Path("newClass")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createClass(ClassWrapper newC,@Context UriInfo uriInfo) {
		Classes newClass = new Classes();
		newClass.setIdClass(newC.getIdClass());
		newClass.setClassName(newC.getClassName());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
        GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if(newC!=null && classDao.create(newClass)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	@Path("allClasses")
	@GET
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response seeClass(@Context UriInfo uriInfo) {
		List<Classes>allC=classDao.findAll();
		List<ClassResponse> allCResp=new ArrayList<ClassResponse>();
		System.out.println(allC);
		for(Classes c : allC) {
			ClassResponse cr= new ClassResponse();
			cr.setIdClass(c.getIdClass());
			cr.setClassName(c.getClassName());
			String uri=uriInfo.getAbsolutePathBuilder().build().toString();
    		cr.addLink(uri, "self", "GET");
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
    				.build().toString();
            cr.addLink( uri, "general services", "GET");
            allCResp.add(cr);
		}
		GenericEntity<List<ClassResponse>> e = new GenericEntity<List<ClassResponse>>(allCResp) {};
		if(allC.size()>0) {
			return Response.status(Response.Status.OK).entity(e).build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	@Path("newTeacherCourse/{teacher_id}/{course_id}")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createAssociationTeacher(@PathParam("teacher_id") String teacherId,
			 @PathParam("course_id") int courseId,@Context UriInfo uriInfo) {
		Course course= courseDao.get(courseId);
		Teacher teacher = teacherDao.get(teacherId);
		teacher.getCourseKeep().add(course);
		course.setTeacher(teacher);
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (course!= null && teacher!= null && teacherDao.update(teacher) && courseDao.update(course)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	@Path("newClassCourse/{class_id}/{course_id}")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	 public Response createAssociationClass(@PathParam("class_id") int classId,
			 @PathParam("course_id") int courseId,@Context UriInfo uriInfo) {
		Course course= courseDao.get(courseId);
		Classes teacher = classDao.get(classId);
		CourseClassAssociation cca = new CourseClassAssociation();
		cca.setPrimaryKey(new CourseClassAssociation_Id());
		cca.getPrimaryKey().setClass_id(classId);
		cca.getPrimaryKey().setCourse_id(courseId);
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (course!= null && teacher!= null && ccaDao.create(cca)) {
			 return Response.status(Response.Status.OK).entity(e).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	@Path("newPayment")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	
	 public Response issuePayment(Payement newP,@Context UriInfo uriInfo) {
		newP.setPayID(paymentDao.maxid());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newP !=null && paymentDao.create(newP))
			return Response.status(Response.Status.OK).entity(e).build();
		else
			return Response.status(Response.Status.BAD_REQUEST).build();
	}

	@Path("timeTable/{course_id}")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
    public Response create(@PathParam("course_id")int id ,TimeTableWrapper newHours
    		,@Context UriInfo uriInfo) {
		System.out.println("entro");
		System.out.println(newHours.getFinishTime());
		TimeTable newH = new TimeTable();
		newH.setIdTime(new TimeTable_Id());
		newH.setClassRoom(newHours.getClassRoom());
		newH.getIdTime().setCourseId(newHours.getCourseId());
		newH.getIdTime().setStartingTime(newHours.getStartingTime());
		newH.getIdTime().setFinishTime(newHours.getFinishTime());
		newH.getIdTime().setDay(newHours.getDay());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if ( timeTableDao.create(newH)) {
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
