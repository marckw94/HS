package org.marcoWenzel.middleware.highSchool.wrapper;

import java.util.Date;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class NotificationWrapper {
	private String receiver;
	private String Content;
	private String contentType;

	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String userName) {
		this.receiver = userName;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
