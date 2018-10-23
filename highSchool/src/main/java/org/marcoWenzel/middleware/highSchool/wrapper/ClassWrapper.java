package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClassWrapper {
	private int idClass;
	private String className;
	public int getIdClass() {
		return idClass;
	}
	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
