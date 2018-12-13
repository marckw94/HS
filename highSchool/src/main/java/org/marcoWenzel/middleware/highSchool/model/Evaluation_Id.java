package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Embeddable
public class Evaluation_Id  implements Serializable {
	
	private Course courseId;
	
 	private Student studentId;
	public Evaluation_Id() {}
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="COURSE_ID")
	public Course getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Course courseId) {
		this.courseId = courseId;
	}
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="STUDENT_ID")
	public Student getStudentId() {
		return studentId;
	}
	public void setStudentId(Student studentId) {
		this.studentId = studentId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
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
		Evaluation_Id other = (Evaluation_Id) obj;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		return true;
	}
	
}
