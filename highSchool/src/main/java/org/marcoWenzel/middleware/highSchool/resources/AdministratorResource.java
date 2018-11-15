package org.marcoWenzel.middleware.highSchool.resources;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
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
import javax.xml.ws.spi.http.HttpContext;

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
import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.exception.NoContentException;
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
import org.marcoWenzel.middleware.highSchool.response.ClassCourseResponse;
import org.marcoWenzel.middleware.highSchool.response.ClassResponse;
import org.marcoWenzel.middleware.highSchool.response.CourseResponse;
import org.marcoWenzel.middleware.highSchool.response.ParentResponse;
import org.marcoWenzel.middleware.highSchool.response.StudentResponse;
import org.marcoWenzel.middleware.highSchool.response.TeacherResponse;
import org.marcoWenzel.middleware.highSchool.response.TimeTableResponse;
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

import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.research.ws.wadl.Request;
//@Secured({Category.Admin})
@Path("Admin")
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
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
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
    SimpleDateFormat timeTablesdf = new SimpleDateFormat("HH:mm:ss");
   
	@GET 
	@Path("allCourses")
	public Response getAllClasses(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Course>allCourses=courseDao.findAll();
		List <CourseResponse> crs= new ArrayList<CourseResponse>();
		for (Course i : allCourses) {
    		CourseResponse newClass = new CourseResponse();
    		newClass.setClassRoom(i.getClassRoom());
    		newClass.setCourseDescription(i.getCourseDescription());
    		newClass.setCourseName(i.getCourseName());
    		newClass.setIdCourse(i.getIdCourse());
    		String uri;
    		if (crs.isEmpty()) {
	    		uri=uriInfo.getAbsolutePathBuilder().build().toString();
	    		newClass.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            newClass.addLink( uri, "general services", "GET");
	    		
    		}
    		
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Course").path(String.valueOf(newClass.getIdCourse()))
    				.build().toString();
    		newClass.addLink(uri, "see specific class", "GET");
    		crs.add(newClass);
		}
		GenericEntity<List<CourseResponse>> e = new GenericEntity<List<CourseResponse>>(crs) {};
		if(allCourses.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
		}
	}
	
	@GET 
	@Path("allParents")
	
	public Response getAllParents(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Parent>allParents=parentDao.findAll();
		List <ParentResponse> crs= new ArrayList<ParentResponse>();
		for (Parent i : allParents) {
    		ParentResponse newPar = new ParentResponse();
    		newPar.setUsername(i.getUsername());
    		newPar.setPassword("*****");
    		newPar.setName(i.getName());
    		newPar.setSurname(i.getSurname());
    		String uri;
    		if (crs.isEmpty()) {
	    		uri=uriInfo.getAbsolutePathBuilder().build().toString();
	    		newPar.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            newPar.addLink( uri, "general services", "GET");
	            uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newParent")
	    				.build().toString();
	            newPar.addLink( uri, "add new Parent", "POST");
    		}
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Parent")
    				.path(newPar.getUsername()).build().toString();
    		newPar.addLink( uri, "see specif parent", "GET");
    		crs.add(newPar);
		}
		GenericEntity<List<ParentResponse>> e = new GenericEntity<List<ParentResponse>>(crs) {};
		if(allParents.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
	}

	@GET 
	@Path("allTeachers")	
	public Response getAllTeachers(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Teacher>allTeachers=teacherDao.findAll();
		List <TeacherResponse> crs= new ArrayList<TeacherResponse>();
		for (Teacher i : allTeachers) {
    		TeacherResponse newTeach = new TeacherResponse();
    		newTeach.setTeacherId(i.getTeacherId());
    		newTeach.setPassword("*****");
    		newTeach.setName(i.getName());
    		newTeach.setSurname(i.getSurname());
    		String uri;
    		if (crs.isEmpty()) {
	    		uri=uriInfo.getAbsolutePathBuilder().build().toString();
	    		newTeach.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            newTeach.addLink( uri, "general services", "GET");
	            uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newTeacher")
	    				.build().toString();
	    		newTeach.addLink( uri, "add new teacher", "POST");
    		}
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Teacher")
    				.path(newTeach.getTeacherId()).build().toString();
    		newTeach.addLink( uri, "see specif teacher", "GET");
    		crs.add(newTeach);
		}
		GenericEntity<List<TeacherResponse>> e = new GenericEntity<List<TeacherResponse>>(crs) {};
		if(allTeachers.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
	}
	@GET 
	@Path("allStudents")

	public Response getAllStudents(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Student>allStudents=studentDao.findAll();
		List <StudentResponse> crs= new ArrayList<StudentResponse>();
		for (Student i : allStudents) {
    		StudentResponse newStud = new StudentResponse();
    		newStud.setRollNo(i.getRollNo());
    		newStud.setLastName(i.getLastName());
    		newStud.setName(i.getName());
    		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
    		if (crs.isEmpty()) {
	    		newStud.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            newStud.addLink( uri, "general services", "GET");
	            uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("parent_id").path("newSon")
	    				.build().toString();
	            newStud.addLink( uri, "add new students", "POST");
    		}
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Student").path(String.valueOf(newStud.getRollNo()))
    				.build().toString();
    		newStud.addLink( uri, "see specific student", "GET");
    		crs.add(newStud);
		}
		GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(crs) {};
		if(allStudents.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
	}
	
	@GET
	public Response getAdminServices(@PathParam("user_id")String id,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Link> uris = new ArrayList<Link>();
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("enrollment")
				.build().toString();
		addLinkToList(uris, uri, "enroll student", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allStudents")
				.build().toString();
		addLinkToList(uris, uri, "see students", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allParents")
				.build().toString();
		addLinkToList(uris, uri, "see parents", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allTeachers")
				.build().toString();
		addLinkToList(uris, uri, "see teachers", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allCourses")
				.build().toString();
		addLinkToList(uris, uri, "see Courses", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allClassCourse")
				.build().toString();
		addLinkToList(uris, uri, "see class course assotiations", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("allClasses")
				.build().toString();
		addLinkToList(uris, uri, "see classes", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("studentsPerClass").path("[class_id]")
				.build().toString();
		addLinkToList(uris, uri, "see all the students of a specific class", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newAdministrator")
				.build().toString();
		addLinkToList(uris, uri, "create new admin", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newParent")
				.build().toString();
		addLinkToList(uris, uri, "create new Parent", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newTeacher")
				.build().toString();
		addLinkToList(uris, uri, "create new Teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newCourse")
				.build().toString();
		addLinkToList(uris, uri, "create new course", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newTeacherCourse").path("teacher_id").path("course_id")
				.build().toString();
		addLinkToList(uris, uri, "assign a course to a teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newClassCourse").path("class_id").path("course_id")
				.build().toString();
		addLinkToList(uris, uri, "assign a class to a course", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newPayment")
				.build().toString();
		addLinkToList(uris, uri, "issue new payment", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("newClass")
				.build().toString();
		addLinkToList(uris, uri, "create new class", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("newParentNotif").path("user_id")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to a single parent", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("newTeacherNotif").path("user_id")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to a single teacher", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("allParents")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all parents", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("allTeachers")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all teachers", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("classParents").path("class_id")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to all the parents of a specific class ", "POST");
		uri=uriInfo.getBaseUriBuilder().path(NotificationResource.class)
				.path("classTeachers").path("class_id")
				.build().toString();
		addLinkToList(uris, uri, "issue notification to the teachers of a specific class ", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.path("TimeTable").path("class_id")
				.build().toString();
		addLinkToList(uris, uri, "add new class hours", "POST");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	}
	@PUT
	@Path("enrollment")

	public Response enrollStud(EnrollRequest newEnroll,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Student student = studentDao.get(newEnroll.getIdStud());
		if (student==null)
			 throw new DataNotFoundException();
		if (student.getEnrolledClass()!=null) {
			 throw new DataNotFoundException();		}
		Classes enrollClass =classDao.get(newEnroll.getIdClass());
		enrollClass.setEnrolledStud(new ArrayList<Student>());
		enrollClass.getEnrolledStud().add(student);
		student.setEnrolledClass(enrollClass);
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
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("studentsPerClass").path(String.valueOf(enrollClass.getIdClass()))
				.build().toString();
        addLinkToList(uris, uri, "see students of enrolled class", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Student").path(String.valueOf(student.getRollNo()))
				.build().toString();
        addLinkToList(uris, uri, "see enrolled student", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Class").path((String.valueOf(enrollClass.getIdClass())))
				.build().toString();
        addLinkToList(uris, uri, "see class of enrollment", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		 if (classDao.update(enrollClass) && studentDao.update(student)) {
	            return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
	        }
	        else {
	        	 throw new DataNotFoundException();	        }
	}
	
	@GET
	@Path("studentsPerClass/{class_id}")
	 public Response allStudEnrolled(@PathParam("class_id") int idClass,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Classes> ccList = classDao.findAll();
		List<StudentResponse> studList = new ArrayList<StudentResponse>();
		if (ccList == null )
			 throw new DataNotFoundException();
		else 
			for (Classes cc : ccList) {
				if (cc.getIdClass()==idClass) {
					for (Student s : cc.getEnrolledStud()) {
						StudentResponse newStud= new StudentResponse();
						newStud.setRollNo(s.getRollNo());
						newStud.setLastName(s.getLastName());
						newStud.setName(s.getName());
						String uri;
						if (studList.isEmpty()) {
							uri= uriInfo.getAbsolutePathBuilder().build().toString();
							newStud.addLink(uri, "self", "GET");
							uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				    				.build().toString();
				        	newStud.addLink(uri, "general services","GET");
				        	uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("enrollment")
				    				.build().toString();
				        	newStud.addLink(uri, "enroll a new student","PUT");
						}
						uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Student").path(String.valueOf(newStud.getRollNo()))
			    				.build().toString();
			    		newStud.addLink( uri, "see specific student", "GET");
						studList.add(newStud);
					}	
				}
			}
		GenericEntity<List<StudentResponse>> e = new GenericEntity<List<StudentResponse>>(studList) {};
		if(studList.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
	}

	//serve una sorta schermata di home
	@Path("newAdministrator")
	@POST
	
	public Response createAdmin(Administrator newA,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		String accept=h.getHeaderString(HttpHeaders.CONTENT_TYPE);
		System.out.println("media used: "+ accept);
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
	            return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
	        }
	        else {
	        	 throw new DataNotFoundException();
	        	 }
	
	}
	
	@Path("newParent")
	@POST
	 public Response createParent(ParentWrapper newP,@Context UriInfo  uriInfo,@Context HttpHeaders h ) {
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
			 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	
	@Path("{parent_id}/newSon")
	@POST
	 public Response createSon(Wrapper newS,@PathParam("parent_id") String parentS,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Parent parent = parentDao.get(parentS);
		if (parent==null)
			throw new DataNotFoundException();
		Student newStud = new Student();
		newStud.setName(newS.getName());
		newStud.setLastName(newS.getLastName());
		if (newStud.getLastName()==null)
			throw new DataNotFoundException();
		newStud.setParentUsername(parent);
		newStud.setRollNo(studentDao.maxid("rollNo"));
		parent.getSon().add(newStud);
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("enrollment").build().toString();
		addLinkToList(uris, uri, "enroll student to a class", "PUT");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("allClasses")
				.build().toString();
        addLinkToList(uris, uri, "see classes", "GET");
        /*
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("allParents")
				.build().toString();
        addLinkToList(uris, uri, "see all parents", "GET");*/
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};

		if(newStud!= null && studentDao.create(newStud) && parentDao.update(parent)) {
			return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	
	
	@Path("newTeacher")
	@POST
	
	 public Response createTeacher(TeacherWrapper newT,@Context UriInfo uriInfo,@Context HttpHeaders h) {
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
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("allTeachers")
				.build().toString();
        addLinkToList(uris, uri, "see all teachers", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newCourse")
				.build().toString();
        addLinkToList(uris, uri, "add new course", "POST");
		for(Course c : courseList) {
			if (c.getTeacher()==null) {
				uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
						.path("association").path(newTc.getTeacherId())
						.path(Long.toString(c.getIdCourse())).build().toString();
				addLinkToList(uris, uri, "assign to this course", "PUT");
				
			}
		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newlog!= null && loginDao.create(newlog) && newTc!=null && teacherDao.create(newTc)) {
			 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	
	@Path("newCourse")
	@POST

	 public Response createCourse(CourseWrapper newC,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Teacher> teacherList = teacherDao.findAll();
		Course newCourse= new Course();
		newCourse.setClassRoom(newC.getClassRoom());
		newCourse.setCourseDescription(newC.getCourseDescription());
		newCourse.setCourseName(newC.getCourseName());
		newCourse.setIdCourse(courseDao.maxid("idCourse"));
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("allCourses")
				.build().toString();
        addLinkToList(uris, uri, "see all courses", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newTeacher")
				.build().toString();
        addLinkToList(uris, uri, "create new teacher", "POST");
		for(Teacher t : teacherList) {
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newTeacherCourse")
					.path(t.getTeacherId()).path(Long.toString(newCourse.getIdCourse()))
					.build().toString();
			addLinkToList(uris, uri, "assign to a teacher", "PUT");
			
		}
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newCourse!= null && courseDao.create(newCourse)) {
			 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	@Path("newClass")
	@POST

	 public Response createClass(ClassWrapper newC,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Course> courseList= courseDao.findAll();
		Classes newClass = new Classes();
		newClass.setIdClass(classDao.maxid("idClass"));
		newClass.setClassName(newC.getClassName());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("allClasses")
				.build().toString();
        addLinkToList(uris, uri, "see all classes", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newCourse")
				.build().toString();
        addLinkToList(uris, uri, "add new course", "POST");
        for(Course c: courseList) {
        	 uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newClassCourse")
     				.path(String.valueOf(newClass.getIdClass())).path(String.valueOf(c.getIdCourse())).build().toString();
        	 addLinkToList(uris, uri, "create new class course association", "PUT");
        }
        GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if(newC!=null && classDao.create(newClass)) {
			 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	
	@Path("allClassCourse")
	@GET
	public Response seeAssotiation(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List <CourseClassAssociation> listCca= ccaDao.findAll();
		List<Integer> listOfClasses = new ArrayList<Integer>();
		List<ClassCourseResponse> ccrList = new ArrayList<ClassCourseResponse>();
		for(CourseClassAssociation cca : listCca) {
			if (!listOfClasses.contains(cca.getPrimaryKey().getClass_id())) 
				listOfClasses.add(cca.getPrimaryKey().getClass_id());
			
		}
		System.out.println(listOfClasses);
		for (Integer idClass : listOfClasses ) {
			ClassCourseResponse newResp = new ClassCourseResponse();
			Classes c = classDao.get(idClass);
			newResp.setIdClass(c.getIdClass());
			Course courseSelection=null;
			newResp.setClassName(c.getClassName());
			newResp.setCourseList(new ArrayList<String>());
			String uri;
			if (ccrList.isEmpty()) {
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
	    		newResp.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            newResp.addLink( uri, "general services", "GET");        
			}
			for (CourseClassAssociation cca : listCca) {
				if (cca.getPrimaryKey().getClass_id()==c.getIdClass()) {
					courseSelection= courseDao.get(cca.getPrimaryKey().getCourse_id());
					newResp.getCourseList().add(courseSelection.getCourseName());
					uri =uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Course")
							.path(String.valueOf(courseSelection.getIdCourse()))
							.build().toString();
					newResp.addLink( uri, "see course:"+courseSelection.getCourseName(), "GET");
				}
			}
			
			uri =uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Class")
					.path(String.valueOf(c.getIdClass()))
					.build().toString();
			newResp.addLink( uri, "see class:"+c.getClassName(), "GET");
			
		
            ccrList.add(newResp);
     
		}
		GenericEntity<List<ClassCourseResponse>> e = new GenericEntity<List<ClassCourseResponse>>(ccrList) {};
		if(ccrList.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
		
	}
	@Path("allClasses")
	@GET

	 public Response seeClass(@Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Classes>allC=classDao.findAll();
		List<ClassResponse> allCResp=new ArrayList<ClassResponse>();
		String uri;
		for(Classes c : allC) {
			ClassResponse cr= new ClassResponse();
			cr.setIdClass(c.getIdClass());
			cr.setClassName(c.getClassName());
			if (allCResp.isEmpty()) {
				uri=uriInfo.getAbsolutePathBuilder().build().toString();
	    		cr.addLink(uri, "self", "GET");
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
	    				.build().toString();
	            cr.addLink( uri, "general services", "GET");
	            uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newClass")
	    				.build().toString();
	            cr.addLink( uri, "create new class", "POST");
			}
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Class").path(String.valueOf(c.getIdClass()))
    				.build().toString();
            cr.addLink( uri, "see class in specific", "GET");
            allCResp.add(cr);
		}
		GenericEntity<List<ClassResponse>> e = new GenericEntity<List<ClassResponse>>(allCResp) {};
		if(allC.size()>0) {
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}else {
			throw new NoContentException();
			}
	}
	@Path("Class/{class_id}")
	@GET
	public Response createAssociationTeacher(@PathParam("class_id") int classId,
			 @Context UriInfo uriInfo,@Context HttpHeaders h) {
		Classes specifiClass= classDao.get(classId);
		List<Course> courseList = courseDao.findAll();
		if (specifiClass==null)
			throw new DataNotFoundException();
		ClassResponse cresp= new ClassResponse();
		cresp.setIdClass(specifiClass.getIdClass());
		cresp.setClassName(specifiClass.getClassName());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		cresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("studentsPerClass").path(String.valueOf(classId))
				.build().toString();
		cresp.addLink(uri, "see students of this class", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("enrollment")
				.build().toString();
    	cresp.addLink(uri, "enroll a new student","PUT");
    	uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("TimeTable")
    			.path(String.valueOf(classId))
				.build().toString();
    	cresp.addLink(uri, "see the timeTable class","PUT");
    	for(Course c: courseList) {
    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.path("newClassCourse").path(String.valueOf(specifiClass.getIdClass()))
					.path(String.valueOf(c.getIdCourse())).build().toString();
			cresp.addLink(uri, "assign "+c.getCourseName()+" course to this class ", "POST");
    	}
		return Response.status(Response.Status.OK).entity(cresp).type(negotiation(h)).build();
	}
	@Path("Student/{stud_id}")
	@GET
	public Response getSpecificStudent(@PathParam("stud_id") int studId,
			 @Context UriInfo uriInfo,@Context HttpHeaders h) {
		Student specificStudent= studentDao.get(studId);
		if (specificStudent==null)
			throw new DataNotFoundException();
		StudentResponse sresp= new StudentResponse();
		sresp.setRollNo(specificStudent.getRollNo());
		sresp.setLastName(specificStudent.getLastName());
		sresp.setName(specificStudent.getName());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		sresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        sresp.addLink( uri, "general services", "GET");
		return Response.status(Response.Status.OK).entity(sresp).type(negotiation(h)).build();
	}
	@Path("Parent/{parent_id}")
	@GET
	public Response getSpecificParent(@PathParam("parent_id") String parentId,
			 @Context UriInfo uriInfo,@Context HttpHeaders h) {
		Parent specificParent= parentDao.get(parentId);
		if (specificParent==null)
			throw new DataNotFoundException();
		ParentResponse presp= new ParentResponse();
		presp.setUsername(specificParent.getUsername());
		presp.setSurname(specificParent.getSurname());
		presp.setName(specificParent.getName());
		presp.setPassword("*****");
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		presp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        presp.addLink( uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path(presp.getUsername())
        		.path("newSon").build().toString();
        presp.addLink( uri, "add new son", "POST");
        return Response.status(Response.Status.OK).entity(presp).type(negotiation(h)).build();
	}
	
	@Path("Teacher/{teacher_id}")
	@GET
	public Response getSpecificTeacher(@PathParam("teacher_id") String teacherId,
			 @Context UriInfo uriInfo,@Context HttpHeaders h) {
		List<Course> courseList= courseDao.findAll();
		Teacher specificTeacher= teacherDao.get(teacherId);
		if (specificTeacher==null)
			throw new DataNotFoundException();
		TeacherResponse tresp= new TeacherResponse();
		tresp.setTeacherId(specificTeacher.getTeacherId());
		tresp.setSurname(specificTeacher.getSurname());
		tresp.setName(specificTeacher.getName());
		tresp.setPassword("*****");
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		tresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        tresp.addLink( uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newCourse")
				.build().toString();
        tresp.addLink(uri, "add new course", "POST");
        for(Course c : courseList) {
			if (c.getTeacher()==null) {
				uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
						.path("newTeacherCourse").path(specificTeacher.getTeacherId())
						.path(Long.toString(c.getIdCourse())).build().toString();
				tresp.addLink(uri, "assign to this course", "PUT");
				
			}
		}
        return Response.status(Response.Status.OK).entity(tresp).type(negotiation(h)).build();
	}
	@Path("Course/{course_id}")
	@GET
	public Response getSpecificParent(@PathParam("course_id") int courseId,
			 @Context UriInfo uriInfo,@Context HttpHeaders h) {
		Course specificCourse= courseDao.get(courseId);
		List<Classes> classesList= classDao.findAll();
		if (specificCourse==null)
			throw new DataNotFoundException();
		List<Teacher> teacherList=teacherDao.findAll();
		CourseResponse cresp= new CourseResponse();
		cresp.setIdCourse(specificCourse.getIdCourse());
		cresp.setClassRoom(specificCourse.getClassRoom());
		cresp.setCourseName(specificCourse.getCourseName());
		cresp.setCourseDescription(specificCourse.getCourseDescription());
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		cresp.addLink(uri, "self", "GET");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        cresp.addLink( uri, "general services", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("newTeacher")
        		.build().toString();
        cresp.addLink( uri, "add new teacher", "POST");
        for(Teacher t : teacherList) {
		
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.path("newTeacherCourse").path(t.getTeacherId())
					.path(Long.toString(specificCourse.getIdCourse())).build().toString();
			cresp.addLink(uri, "assign teacher to this course", "PUT");

		}
        for(Classes c : classesList) {
    		
			uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
					.path("newClassCourse").path(String.valueOf(c.getIdClass()))
					.path(String.valueOf(specificCourse.getIdCourse())).build().toString();
			cresp.addLink(uri, "assign class to this course ", "POST");

		}
        return Response.status(Response.Status.OK).entity(cresp).type(negotiation(h)).build();
	}
	@Path("newTeacherCourse/{teacher_id}/{course_id}")
	@PUT

	 public Response createAssociationTeacher(@PathParam("teacher_id") String teacherId,
			 @PathParam("course_id") int courseId,@Context UriInfo uriInfo,@Context HttpHeaders h) {
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
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Teacher").path(teacher.getTeacherId())
				.build().toString();
        addLinkToList(uris, uri, "see associate teacher", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Course").path(String.valueOf(course.getIdCourse()))
				.build().toString();
        addLinkToList(uris, uri, "see associate course", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (course!= null && teacher!= null && teacherDao.update(teacher) && courseDao.update(course)) {
			 return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	@Path("newClassCourse/{class_id}/{course_id}")
	@POST

	 public Response createAssociationClass(@PathParam("class_id") int classId,
			 @PathParam("course_id") int courseId,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		Course course= courseDao.get(courseId);
		Classes class1 = classDao.get(classId);
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
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Class").path(String.valueOf(class1.getIdClass()))
				.build().toString();
        addLinkToList(uris, uri, "see associated class", "GET");
        uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("Course").path(String.valueOf(course.getIdCourse()))
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (course!= null && class1!= null && ccaDao.create(cca)) {
			 return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		}
		 throw new DataNotFoundException();
		 }
	@Path("newPayment")
	@POST

	 public Response issuePayment(PaymentWrapper newPW,@Context UriInfo uriInfo,@Context HttpHeaders h) {

		
		Payement newP = new Payement();
		newP.setPayID(paymentDao.maxid("payID"));
		newP.setCost(newPW.getCost());
		newP.setNotificationDate(cal.getTime());
		newP.setParentUsername(newPW.getParentUsername());
		newP.setPayed(newP.isPayed());
		if (newPW.getPayementDate().before(cal.getTime()))
			throw new DataNotFoundException();
		newP.setPayementDate(newPW.getPayementDate());
		newP.setPaymentDescription(newPW.getPaymentDescription());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if (newP !=null && paymentDao.create(newP))
			return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
		else
			 throw new DataNotFoundException();
		}

	
	@Path("TimeTable/{class_id}")
	@GET
	 public Response seeClassTimeTable(@PathParam("class_id")int id 
	    		,@Context UriInfo uriInfo,@Context HttpHeaders h) throws ParseException {
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
				ttList.add(ttresp);
				if (ttList.isEmpty()) {
					uri=uriInfo.getAbsolutePathBuilder().build().toString();
					ttresp.addLink(uri, "self", "GET");
					uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
							.build().toString();
					ttresp.addLink(uri, "general services", "GET");
					uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class).path("TimeTable")
							.path("course_id")
							.build().toString();
					ttresp.addLink(uri, "general services", "GET");
				}
				
			}
		}
		GenericEntity<List<TimeTableResponse>> e = new GenericEntity<List<TimeTableResponse>>(ttList) {};
		if (!ttList.isEmpty())
			return Response.status(Response.Status.OK).entity(e).type(negotiation(h)).build();
		else
			throw new NoContentException();
	}
	
	
	
	@Path("TimeTable/{course_id}")
	@POST

    public Response create(@PathParam("course_id")int id ,TimeTableWrapper newHours
    		,@Context UriInfo uriInfo,@Context HttpHeaders h) {
		
		System.out.println("entro");
		System.out.println(newHours.getFinishTime());
		TimeTable newH = new TimeTable();
		newH.setIdTime(new TimeTable_Id());
		
		
		if (id!=newHours.getCourseId()) {
			throw new DataNotFoundException();
		}
		Course c = courseDao.get(id);
		newH.setClassRoom(c.getClassRoom());
		newH.getIdTime().setCourseId(newHours.getCourseId());
		newH.getIdTime().setStartingTime(newHours.getStartingTime());
		newH.getIdTime().setFinishTime(newHours.getFinishTime());
		newH.getIdTime().setDay(newHours.getDay());
		
		System.out.println(newHours.getDay());
		List<Link> uris = new ArrayList<Link>();
		String uri=uriInfo.getAbsolutePathBuilder().build().toString();
		addLinkToList(uris, uri, "self", "POST");
		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
				.build().toString();
        addLinkToList(uris, uri, "general services", "GET");
		GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
		if ( timeTableDao.create(newH)) {
	        return Response.status(Response.Status.CREATED).entity(e).type(negotiation(h)).build();
	    }
	    else {
	         throw new DataNotFoundException();
	    }
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
