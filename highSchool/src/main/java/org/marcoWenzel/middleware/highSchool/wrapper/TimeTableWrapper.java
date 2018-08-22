package org.marcoWenzel.middleware.highSchool.wrapper;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class TimeTableWrapper {
	 private String classRoom;
	 private int courseId;
	 private Date startingTime;
	 private Date finishTime;
	 private String day;
	public String getClassRoom() {
		return classRoom;
	}
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public Date getStartingTime() {
		return startingTime;
	}
	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

	 
}
