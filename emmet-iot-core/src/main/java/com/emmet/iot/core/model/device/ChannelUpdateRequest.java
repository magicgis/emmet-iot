package com.emmet.iot.core.model;

public class ChannelUpdateRequest {
	private String deviceId;
	private long timestamp;
	private String name;
	private Object value;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ChannelUpdateRequest [deviceId=" + deviceId + ", timestamp=" + timestamp + ", name=" + name + ", value="
				+ value + "]";
	}
	
	

}