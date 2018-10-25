package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlNullPolicy;
import org.hibernate.annotations.Cascade;

/**
 * Root resource (exposed at "myresource" path)
 */
@Entity
@Table(name="STUDENT_INFORMATION")
public class Student implements Serializable {
	@Id 
	private int rollNo;
	
	private String name;
	
	private String lastName;
	@ManyToOne()
	@JoinColumn(name="PARENT_USERNAME")
	private Parent parentUsername;
	@ManyToOne()
	@JoinColumn(name="CLASS_ID" )
	private Classes enrolledClass;
	public Student() {}
	public Student(String name, String lastName) {
		
		this.name = name;
		this.lastName = lastName;
	}
	public Student(int rollNo,String name, String lastName) {
		this.rollNo=rollNo;
		this.name = name;
		this.lastName = lastName;
	}
	
	//@OneToOne(mappedBy="primaryKey.studentId",cascade=CascadeType.ALL)
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Parent getParentUsername() {
		return parentUsername;
	}
	public void setParentUsername(Parent parentUsername) {
		this.parentUsername = parentUsername;
	}
	@Override
	public String toString() {
		return "Student [rollNo=" + rollNo + ", name=" + name + ", lastName=" + lastName + ", parentUsername="
				+ parentUsername + "]";
	}

	public Classes getEnrolledClass() {
		return enrolledClass;
	}
	public void setEnrolledClass(Classes enrolledClass) {
		this.enrolledClass = enrolledClass;
	}
	
	
}
