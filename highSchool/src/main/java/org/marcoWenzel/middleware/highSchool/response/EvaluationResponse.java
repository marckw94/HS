package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.wrapper.CourseWrapper;
import org.marcoWenzel.middleware.highSchool.wrapper.Wrapper;
@XmlRootElement
public class EvaluationResponse {
	private CourseWrapper cw;
	private Wrapper sw;
	private int mark;
	private List<Link> links;  
	public EvaluationResponse() {
		this.links = new ArrayList<>();
		this.cw = new CourseWrapper();
		this.sw= new Wrapper();
		
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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public CourseWrapper getCw() {
		return cw;
	}
	public void setCw(CourseWrapper cw) {
		this.cw = cw;
	}
	public void addLink(String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		links.add(newL);
	}
}
