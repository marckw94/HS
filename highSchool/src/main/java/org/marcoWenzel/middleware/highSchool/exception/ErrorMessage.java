package org.marcoWenzel.middleware.highSchool.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	private String errormessage;
	private int errCode;
	private String documentation;
	public ErrorMessage(String errorMessage, int errCode, String documentation) {
		this.errormessage = errorMessage;
		this.errCode = errCode;
		this.documentation = documentation;
	}
	public ErrorMessage() {}
	public String getErrorMessage() {
		return errormessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errormessage = errorMessage;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
}
