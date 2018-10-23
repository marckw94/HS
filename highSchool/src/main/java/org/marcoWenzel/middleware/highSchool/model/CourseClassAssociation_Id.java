package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class CourseClassAssociation_Id implements Serializable {
	private int class_id;
	private int course_id;
	public int getCourse_id() {
		return course_id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
}
