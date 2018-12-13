package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Notification_Id implements Serializable {
	private int notificationNumber;
	private String receiver;
	public Notification_Id() {}
	public int getNotificationNumber() {
		return notificationNumber;
	}
	public void setNotificationNumber(int notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
}
