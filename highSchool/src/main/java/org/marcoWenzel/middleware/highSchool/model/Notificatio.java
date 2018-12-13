package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="NOTIFICATION")
public class Notificatio  implements Serializable {
	
	 private Notification_Id primaryKey;
	
	 private String Content;
	 private Date sendDate;
	 private String contentType;
	 public Notificatio() {} 
	 @EmbeddedId 
	public Notification_Id getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Notification_Id primaryKey) {
		this.primaryKey = primaryKey;
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
 
}
