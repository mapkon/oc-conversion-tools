package org.openxdata.oc.exception;

public class ExportException extends RuntimeException {

	private static final long serialVersionUID = -3202776431444643227L;

	public ExportException() {
		super();
	}

	public ExportException(String errorMsg) {
		super(errorMsg);
	}

	public ExportException(Throwable ex) {
		super(ex);
	}

	public ExportException(String errorMsg, Throwable ex) {
		super(errorMsg, ex);
	}

}
