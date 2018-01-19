package com.emmet.iot.core.model;

public class Heartbeat {
	
	private String deviceId;
	private long sequence;
	private long timestamp;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Heartbeat [deviceId=" + deviceId + ", sequence=" + sequence + ", timestamp=" + timestamp + "]";
	}
	
	

}
