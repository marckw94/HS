package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class CourseWrapper {
	private int idCourse;
	private String courseName;
	private String courseDescription;
	private String classRoom;
	public int getIdCourse() {
		return idCourse;
	}
	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDescription() {
		return courseDescription;
	}
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	public String getClassRoom() {
		return classRoom;
	}
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	
}
