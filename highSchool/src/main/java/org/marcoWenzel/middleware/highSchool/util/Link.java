package org.marcoWenzel.middleware.highSchool.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link {
	private String link;
	private String rel;
	private String type;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
