package org.marcoWenzel.middleware.highSchool.wrapper;

import java.util.Date;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Table
public class AppointmentWrapper {
	private int appointmentId;
	private String parentUsername;
	private String teacherId;
	private Date appointmentDate;
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


	public int getAppointmentId() {
		return appointmentId;
	}


	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
}
