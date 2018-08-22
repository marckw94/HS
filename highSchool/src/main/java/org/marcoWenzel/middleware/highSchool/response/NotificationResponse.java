package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;
@XmlRootElement
public class NotificationResponse {
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	private int notificationNumber;
	private String userName;
	private String Content;
	private Date sendDate;
	private String contentType;
	private List<Link> links  = new ArrayList<>();
	public int getNotificationNumber() {
		return notificationNumber;
	}
	public void setNotificationNumber(int notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void addLink(String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		links.add(newL);
	}
}
