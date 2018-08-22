package org.marcoWenzel.middleware.highSchool.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.marcoWenzel.middleware.highSchool.util.Link;
@XmlRootElement
public class PaymentResponse {
	private int payID;
	private String parentUsername;
	private String paymentDescription;
	private double cost;
	private boolean isPayed;
	private Date notificationDate;
	private Date payementDate;
	private List<Link> links  = new ArrayList<>();

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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public void addLink(String url,String rel,String type) {
		Link newL= new Link();
		newL.setLink(url);
		newL.setRel(rel);
		newL.setType(type);
		links.add(newL);
	}
}
