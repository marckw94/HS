package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table
public class EnrollRequest {
	private int idStud;
	private int idClass;
	public int getIdStud() {
		return idStud;
	}
	public void setIdStud(int idStud) {
		this.idStud = idStud;
	}
	public int getIdClass() {
		return idClass;
	}
	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}
}
