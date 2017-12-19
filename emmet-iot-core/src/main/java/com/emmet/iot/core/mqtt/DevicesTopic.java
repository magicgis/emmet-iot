package com.emmet.iot.core.mqtt;

public class DevicesTopic {
	static final String DEVICE_PREFIX = "/devices/";
	private static final String HEARTBEAT = "heartbeat";

	public DeviceTopic device(String deviceId) {
		return new DeviceTopic(deviceId);
	}
	
	public String status() {
		return DEVICE_PREFIX + "status";
	}


	public static String heartbeat() {
		return DEVICE_PREFIX + HEARTBEAT;
	}

}
