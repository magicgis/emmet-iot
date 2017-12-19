package com.emmet.iot.manager.exception;

public class UpdateChannleException extends Exception {
	private static final long serialVersionUID = 1L;

	public UpdateChannleException(String deviceId, String channelName, String errorMsg) {
		super("Fail to update device: " + deviceId + "; channle: " + channelName + "; cause: " + errorMsg);
	}

}
