package com.emmet.iot.core.model;

public class ChannelStatus {
	private long lastUdateTime;
	private String name;
	private Object value;
	private boolean online;

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public long getLastUdateTime() {
		return lastUdateTime;
	}

	public void setLastUdateTime(long lastUdateTime) {
		this.lastUdateTime = lastUdateTime;
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

}