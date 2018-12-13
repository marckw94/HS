package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
