package com.emmet.iot.manager.exception;

public class RegisterExistedDeviceException extends Exception {
	private static final long serialVersionUID = 1L;

	public RegisterExistedDeviceException(String errorMsg) {
		super(errorMsg);
	}

}
