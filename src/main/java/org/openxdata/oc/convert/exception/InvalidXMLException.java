package org.openxdata.oc.convert.exception;


public class InvalidXMLException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidXMLException(String string, Exception e) {
		super(string, e);
	}

}
