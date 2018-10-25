package org.marcoWenzel.middleware.highSchool.wrapper;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClassWrapper {
	private String className;

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
