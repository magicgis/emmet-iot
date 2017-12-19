package com.emmet.iot.manager.exception;

public class DeviceNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public DeviceNotFoundException(String deviceId) {
		super("Device " + deviceId + " not found.");
		
	}

}
