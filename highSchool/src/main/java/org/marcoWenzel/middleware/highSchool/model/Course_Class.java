package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@AssociationOverrides({
	@AssociationOverride(name="primaryKey.studentId",joinColumns=@JoinColumn(name="STUDENT_ID")),
	@AssociationOverride(name="primaryKey.courseId",joinColumns=@JoinColumn(name="COURSE_ID"))
	})
@Entity
public class Course_Class  implements Serializable {
	
	private Course_Class_Id primaryKey;
	private boolean isPass;
	private int mark;
	public Course_Class () {}
	@EmbeddedId 
	public Course_Class_Id getId() {
		return primaryKey;
	}
	public void setId(Course_Class_Id id) {
		this.primaryKey = id;
	}
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
}
