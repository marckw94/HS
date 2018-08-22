package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Embeddable
public class Appointment_Id  implements Serializable {
	@Column(length=255)
	private Teacher teacher;
	
	@Column(length=255)
	private Parent parents;
	private Date appointmentDate;
	public Appointment_Id() {}
	
	public Appointment_Id(Teacher teacher, Parent parents, Date date) {

		this.teacher = teacher;
		this.parents = parents;
		this.appointmentDate=date;
	}

	
	@OneToOne(cascade=CascadeType.ALL)
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	@OneToOne(cascade=CascadeType.ALL)
	public Parent getParents() {
		return parents;
	}
	public void setParents(Parent parents) {
		this.parents = parents;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parents == null) ? 0 : parents.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointment_Id other = (Appointment_Id) obj;
		if (parents == null) {
			if (other.parents != null)
				return false;
		} else if (!parents.equals(other.parents))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		return true;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	

}
