package org.marcoWenzel.middleware.highSchool.model;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
@AssociationOverrides({
	@AssociationOverride(name="primaryKey.teacher",joinColumns=@JoinColumn(name="TEACHER_ID")),
	@AssociationOverride(name="primaryKey.parents",joinColumns=@JoinColumn(name="PARENT_USERNAME"))
	})
@Entity
@Table(name="APPOINTMENT")
public class Appointment  implements Serializable {
	
	private Appointment_Id primaryKey;
	
	public Appointment() {}
	@EmbeddedId
	public Appointment_Id getId() {
		return primaryKey;
	}

	public void setId(Appointment_Id id) {
		this.primaryKey = id;
	}

}
