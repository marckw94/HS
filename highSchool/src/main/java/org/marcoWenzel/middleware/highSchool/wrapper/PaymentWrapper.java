package org.marcoWenzel.middleware.highSchool.wrapper;



import java.util.Date;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class PaymentWrapper {
	private String parentUsername;
	private String paymentDescription;
	private double cost;
	private boolean isPayed;
	private Date notificationDate;
	private Date payementDate;
	public PaymentWrapper() {}
	
	public String getParentUsername() {
		return parentUsername;
	}
	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}
	public String getPaymentDescription() {
		return paymentDescription;
	}
	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public boolean isPayed() {
		return isPayed;
	}
	public void setPayed(boolean isPayed) {
		this.isPayed = isPayed;
	}
	public Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	public Date getPayementDate() {
		return payementDate;
	}
	public void setPayementDate(Date payementDate) {
		this.payementDate = payementDate;
	}
	
	
	
}
