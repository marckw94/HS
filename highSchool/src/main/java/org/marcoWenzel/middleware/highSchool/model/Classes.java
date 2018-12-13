package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Classes implements Serializable{
	
	private int idClass;
	
	private String className;
	private Collection<Student> enrolledStud= new ArrayList<Student>();

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

	
}
