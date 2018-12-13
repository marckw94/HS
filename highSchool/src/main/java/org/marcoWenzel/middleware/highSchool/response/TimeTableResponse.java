package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.marcoWenzel.middleware.highSchool.util.Link;
@XmlRootElement
@XmlType(propOrder = {"classRoom", "courseId", "day","startingTime","finishTime", "links"})
public class TimeTableResponse {
	
		 private String classRoom;
		 private int courseId;
		 private String startingTime;
		 private String finishTime;
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
	
	
		public String getStartingTime() {
			return startingTime;
		}
		public void setStartingTime(String startingTime) {
			this.startingTime = startingTime;
		}
		public String getFinishTime() {
			return finishTime;
		}
		public void setFinishTime(String finishTime) {
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
