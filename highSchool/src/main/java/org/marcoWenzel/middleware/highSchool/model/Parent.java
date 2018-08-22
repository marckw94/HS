package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
	 //vedere se funziona sennò tornare alla versione precedente
	//@JoinColumn(name="SON_ENROLL_NUMBER")
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
	//@OneToOne(mappedBy="primaryKey.parents")
	//@OneToMany(mappedBy="primaryKeyApp.parents")
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
	/*public Set<Student> getSon() {
		return son;
	}
	public void setSon(Set<Student> son) {
		this.son = son;
	}*/
	@Override
	public String toString() {
		return "Parent [username=" + username + ", name=" + name + ", surname=" + surname + ", password=" + password
				+ "]";
	}
	
}
