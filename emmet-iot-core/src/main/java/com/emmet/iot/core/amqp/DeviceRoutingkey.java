package com.emmet.iot.core.amqp;

public class DeviceRoutingkey {
	private static final String PREFIX = "device";
	private static final String UPDATE = "update";
	private static final String STATUS = "status";
	private static final String DELIMITER = ".";

	private String device;
	private String channel;

	public DeviceRoutingkey(String device) {
		this.device = device;
	}

	public DeviceRoutingkey cannel(String channel) {
		this.channel = channel;

		return this;
	}

	public String status() {
		return PREFIX + DELIMITER + device + DELIMITER + channel + DELIMITER + STATUS;
	}

	public String update() {
		return PREFIX + DELIMITER + device + DELIMITER + channel + DELIMITER + UPDATE;
	}

}