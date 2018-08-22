package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;
@XmlRootElement
public class AppointmentResponse {
	private String parentUsername;
	private String teacherId;
	private Date appointmentDate;
	private List<Link> links  = new ArrayList<>();
	public String getParentUsername() {
		return parentUsername;
	}
	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
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
