package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;
@XmlRootElement
public class TimeTableResponse {
	
		 private String classRoom;
		 private int courseId;
		 private Date startingTime;
		 private Date finishTime;
		 private String day;
		 private List<Link> links  = new ArrayList<>();
		public List<Link> getLinks() {
			return links;
		}
		public void setLinks(List<Link> links) {
			this.links = links;
		}
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
		public void addLink(String url,String rel,String type) {
			Link newL= new Link();
			newL.setLink(url);
			newL.setRel(rel);
			newL.setType(type);
			links.add(newL);
		}
	

		 
}