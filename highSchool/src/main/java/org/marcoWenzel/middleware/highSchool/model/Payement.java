package org.marcoWenzel.middleware.highSchool.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@Table(name="PAYEMENT")
public class Payement  implements Serializable {
	@Id
	private int payID;
	private String parentUsername;
	private String paymentDescription;
	private double cost;
	private boolean isPayed;
	private Date notificationDate;
	private Date payementDate;
	public Payement() {}
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
