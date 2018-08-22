package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
public class Notification_Id implements Serializable {
	private int notificationNumber;
	private String userName;
	public Notification_Id() {}
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
	
}
