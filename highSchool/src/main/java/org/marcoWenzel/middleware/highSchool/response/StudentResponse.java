package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;

@Table
@XmlRootElement
public class StudentResponse {
	
	private int rollNo;

	private String name;

	private String lastName;
	private List<Link> links  = new ArrayList<>();
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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public void addLink(String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		links.add(newL);
	}
}
