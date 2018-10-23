package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.PrimaryKey;
@XmlRootElement
@Entity
public class Classes implements Serializable{
	
	private int idClass;
	
	private String className;
	private Collection<Student> enrolledStud= new ArrayList<Student>();
	//@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER,mappedBy="classBelong")

	//private Collection<Course> keepCourses= new ArrayList<Course>();
	public Classes() {}
	@Id
	public int getIdClass() {
		return idClass;
	}
	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER,mappedBy="enrolledClass")
	public Collection<Student> getEnrolledStud() {
		return enrolledStud;
	}
	
	public void setEnrolledStud(Collection<Student> enrolledStud) {
		this.enrolledStud = enrolledStud;
	}
	/*
	public Collection<Course> getCourses() {
		return keepCourses;
	}
	public void setCourses(Collection<Course> courses) {
		this.keepCourses = courses;
	}*/
	
}
