package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Teacher  implements Serializable {
	@Id
	private String  teacherID;
	private String password;
	private String name;
	private String surname;
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private Collection<Course> courseKeep= new ArrayList<Course>();
	
	public Teacher() {}
	public Teacher(String teacherId) {
		this.teacherID=teacherId;
	}
	public String getTeacherId() {
		return teacherID;
	}
	public void setTeacherId(String teacherId) {
		this.teacherID = teacherId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Collection<Course> getCourseKeep() {
		return courseKeep;
	}
	public void setCourseKeep(Collection<Course> courseKeep) {
		this.courseKeep = courseKeep;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
}
