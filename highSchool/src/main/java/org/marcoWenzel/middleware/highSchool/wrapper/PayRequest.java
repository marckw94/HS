package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class PayRequest {
	private int payID;
	private String parentUsername;
	public int getPayID() {
		return payID;
	}
	public void setPayID(int payID) {
		this.payID = payID;
	}
	public String getParentUsername() {
		return parentUsername;
	}
	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}
	
}
