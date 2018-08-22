package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class Course_ClassWrapper {
	private CourseWrapper cw;
	private Wrapper sw;
	private int mark;
	public Course_ClassWrapper() {
		this.cw = new CourseWrapper();
		this.sw= new Wrapper();
		
	}
	public CourseWrapper getCw() {
		return cw;
	}
	public void setCw(CourseWrapper cw) {
		this.cw = cw;
	}
	public Wrapper getSw() {
		return sw;
	}
	public void setSw(Wrapper sw) {
		this.sw = sw;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	
}
