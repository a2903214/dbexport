package com.oscar.dbexport.util;

import java.sql.SQLException;

public class STException extends SQLException {
	/**
     * 
     */
	private static final long serialVersionUID = -5692298719598983212L;
	private int errorcode;

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public STException(int errorcode, String errorMsg) {
		this(errorcode, errorMsg, (Throwable) null);
	}

	public STException(int errorcode, String errorMsg, Throwable tr) {
		super(errorMsg, "" + errorcode, errorcode, tr);
		this.errorcode = errorcode;
	}

	public static void throwNotImplementException() throws STException {
		throw new STException(STErrorCode.NOT_IMPLEMENT_ERROR, "Not implement!");
	}

}
