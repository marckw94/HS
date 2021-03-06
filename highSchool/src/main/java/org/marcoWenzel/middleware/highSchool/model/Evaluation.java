package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;



@AssociationOverrides({
	@AssociationOverride(name="primaryKey.studentId",joinColumns=@JoinColumn(name="STUDENT_ID")),
	@AssociationOverride(name="primaryKey.courseId",joinColumns=@JoinColumn(name="COURSE_ID"))
	})
@Entity
public class Evaluation  implements Serializable {
	
	private Evaluation_Id primaryKey;
	private boolean isPass;
	private int mark;
	public Evaluation () {}
	
	@EmbeddedId 
	public Evaluation_Id getId() {
		return primaryKey;
	}
	public void setId(Evaluation_Id id) {
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
