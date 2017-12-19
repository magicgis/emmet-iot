package com.emmet.iot.core.model;

import java.util.ArrayList;
import java.util.List;

public class DeviceStatusNotification {

	private String deviceId;
	private boolean online;
	private List<ChannelStatus> channels = new ArrayList<>();


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}



	public List<ChannelStatus> getChannels() {
		return channels;
	}

	public void setChannelValue(String name, String value) {

		ChannelStatus cs = new ChannelStatus();
		cs.setName(name);
		cs.setValue(value);
		cs.setLastUdateTime(System.currentTimeMillis());
		channels.add(cs);
	}

}
