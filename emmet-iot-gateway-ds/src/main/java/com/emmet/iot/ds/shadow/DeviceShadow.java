package com.emmet.iot.ds.shadow;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.emmet.iot.core.config.Constant;
import com.emmet.iot.core.model.ChannelStatus;
import com.emmet.iot.core.model.ChannelUpdateRequest;
import com.emmet.iot.core.model.DeviceDataModel;
import com.emmet.iot.core.model.DeviceStatusListener;
import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.model.Heartbeat;
import com.emmet.iot.core.mqtt.DeviceTopic;
import com.emmet.iot.core.mqtt.DevicesTopic;
import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.util.JsonHelper;
import com.emmet.iot.ds.exception.UpdateChannleException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceShadow extends MqttPubSubClient {

	private String deviceId;
	private long lastHeartbeatTimeMili;
	private DeviceDataModel deviceDataModel;
	private String deviceStatusTopic;
	private String deviceUpdateTopic;
	private String refreshStatusTopic;
	private DeviceStatusListener statusListener;

	private static final Log log = LogFactory.getLog(DeviceShadow.class);

	public DeviceShadow(String deviceId, String mqttBrokerUrl, DeviceStatusListener statusListener)
			throws MqttException {

		this.deviceId = deviceId;
		this.setBrokerUrl(mqttBrokerUrl);
		this.statusListener = statusListener;

		deviceDataModel = new DeviceDataModel(deviceId);

		initMqttTopic();
	}

	public String getDeviceId() {
		return deviceId;
	}

	private void initMqttTopic() throws MqttException {
		deviceStatusTopic = new DevicesTopic().device(deviceId).status();
	}

	@Override
	public void connect() throws MqttException {
		super.connect();
		subscribe(deviceStatusTopic);

		sendRefreshDeviceStatusRequest();
	}

	private void updateShadow(ChannelStatus request) throws UpdateChannleException {
		deviceDataModel.updateModel(request.getName(), request.getValue());

		log.debug("<Device Shdow> Channle status model updated! " + request);
	}

	public void updateDevice(ChannelUpdateRequest request) throws MqttPersistenceException, MqttException {
		publish(new DevicesTopic().device(deviceId).update(), JsonHelper.ObjectToJsonString(request));
	}

	public DeviceDataModel getCurrentStatus() {
		deviceDataModel.setOnline(isOnLine());
		return deviceDataModel;
	}

	@Override
	public void mqttMessageArrived(String topic, MqttMessage msg) {
		log.debug("<Device Shdow> Message arrived! " + " topic: " + topic + " message: " + msg.toString());

		if (topic.equals(deviceStatusTopic)) {
			log.debug("<Device Shdow (" + deviceId + ")> Channel status notification! " + " topic: " + topic
					+ " message: " + msg.toString());
			DeviceStatusNotification status = JsonHelper.JsonStringToObject(msg.toString(),
					DeviceStatusNotification.class);
			for (ChannelStatus channle : status.getChannels()) {

				try {
					this.updateShadow(channle);
				} catch (UpdateChannleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			statusListener.onDeviceStatusChange(this.getStatus());

		} else {
			log.warn("## The message is ignored ## topic: " + topic + " message: " + msg);
		}
	}



	public DeviceStatusNotification getStatus() {
		DeviceStatusNotification status = new DeviceStatusNotification();
		status.setDeviceId(getDeviceId());
		status.setOnline(isOnLine());
		deviceDataModel.getDataChannels().forEach((name, value) -> {
			status.setChannelValue(name, value.toString());
		});

		return status;
	}

	public boolean isOnLine() {
		return System.currentTimeMillis() - lastHeartbeatTimeMili < Constant.HEARTBEAT_THRESHOLD_MILI;
	}

	private void updateHeartbeatStatus(MqttMessage msg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Heartbeat status = mapper.readValue(msg.toString(), Heartbeat.class);
			lastHeartbeatTimeMili = status.getTimestamp();
			if (!this.isOnLine()) {
				sendRefreshDeviceStatusRequest();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateHeartbeatTime(long timeMili) {
		if (!this.isOnLine()) {
			sendRefreshDeviceStatusRequest();
		}
		lastHeartbeatTimeMili = timeMili;
	}

	private void sendRefreshDeviceStatusRequest() {

	}

}
