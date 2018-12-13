package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="TIMETABLE")
public class TimeTable implements Serializable {

	@EmbeddedId
	private TimeTable_Id idTime;
	 private String classRoom;
	public  TimeTable() {
		// TODO Auto-generated constructor stub
	}
	
	public TimeTable_Id getIdTime() {
		return idTime;
	}

	public void setIdTime(TimeTable_Id idTime) {
		this.idTime = idTime;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
 
}
