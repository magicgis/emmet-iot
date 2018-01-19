package com.emmet.iot.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceDataModel {
	
	private String deviceId;
	private boolean online;

	private Map<String, Object> dataChannels = new HashMap<>();

	public DeviceDataModel(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public Map<String, Object> getDataChannels() {
		return dataChannels;
	}

	public void updateModel(String channleName, Object value) {
		dataChannels.put(channleName, value);
	}


	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String toJson() {

		try {
			JSONArray jsonChannelArray = new JSONArray();
			for (Entry<String, Object> entry : dataChannels.entrySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(entry.getKey(), entry.getValue());
				jsonChannelArray.put(jsonObject);
			}

			JSONObject jsonChannels = new JSONObject();
			jsonChannels.put("channels", jsonChannelArray);

			return jsonChannels.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";

	}
}
