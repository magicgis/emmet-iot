package com.emmet.iot.adapter.jdbc.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DeviceStatusLogEntity {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private boolean online;
	private String deviceId;
	private Calendar time;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ChannelValueEntity> channels = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public List<ChannelValueEntity> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelValueEntity> channels) {
		this.channels = channels;
	}
	
	

}
