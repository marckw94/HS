package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name="Course_Class_Association")
public class CourseClassAssociation implements Serializable {
	@EmbeddedId
	private CourseClassAssociation_Id primaryKey;

	public CourseClassAssociation_Id getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(CourseClassAssociation_Id primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}
