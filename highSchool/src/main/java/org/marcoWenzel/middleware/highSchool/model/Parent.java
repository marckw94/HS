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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="PARENT_INFORMATION")
public class Parent  implements Serializable  {
	 @XmlElement
	private String username;
	@XmlElement
	private String name;
	
	@XmlElement
	private String surname;
	@XmlElement
	private String password;

	private Collection<Student> son= new ArrayList<Student>();
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	public Collection<Student> getSon() {
		return son;
	}
	public void setSon(Collection<Student> son) {
		this.son = son;
	}
	
	
	public Parent(String username) {
		this.username = username;
	}
	public Parent() {
		
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
	@Id
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Parent [username=" + username + ", name=" + name + ", surname=" + surname + ", password=" + password
				+ "]";
	}
	
}
