package com.emmet.iot.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.emmet.iot.core.model.ChannelUpdateRequest;
import com.emmet.iot.core.model.DeviceCommand;
import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.model.Heartbeat;
import com.emmet.iot.core.mqtt.DeviceTopic;
import com.emmet.iot.core.mqtt.DevicesTopic;
import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.util.JsonHelper;

@Service
public class MockDevice extends MqttPubSubClient {
	private String deviceId = "device-0001";

	private String statusTopic;
	private String controlTopic;
	private String heartbeatTopic;

	@Value("${gateway.mqtt.broker-url}")
	private String brokerUrl;

	private boolean isShutdown = false;

	private Map<String, Object> channles = new HashMap<>();

	private Boolean poweOn = false;

	@Override
	public void mqttMessageArrived(String topic, MqttMessage msg) {
		log.info(this.deviceId + " received a new message!  topic: " + topic + " message: " + msg);

		if (topic.equals(controlTopic)) {
			DeviceCommand cmd = JsonHelper.JsonStringToObject(new String(msg.getPayload()), DeviceCommand.class);
			if (cmd.getCommand().equals(DeviceCommand.NAME.REPORT_STATUS)) {
				publishStatus(getRandomStatus());
			}

		}

		if (topic.equals(new DevicesTopic().device(deviceId).update())) {
			ChannelUpdateRequest request = JsonHelper.JsonStringToObject(msg.toString(), ChannelUpdateRequest.class);
			System.out.println("-------> update: " + request);
			updateDevice(request);
		}

	}

	private void updateDevice(ChannelUpdateRequest request) {
		String channelName = request.getName();
		channles.put(channelName, request.getValue());
		log.info("Status updated" +  channles);
		DeviceStatusNotification notification = new DeviceStatusNotification();
		notification.setDeviceId(deviceId);
		notification.setChannelValue(channelName, channles.get(channelName).toString());
		log.info("Mock device channel updated." + notification);

		publishStatus(notification);

	}

	private static final Log log = LogFactory.getLog(MockDevice.class);

	@PostConstruct
	public void init() {
		channles.put("D1", false);
		
		this.setBrokerUrl(brokerUrl);
		DeviceTopic mqttTopic = new DevicesTopic().device(deviceId);

		this.controlTopic = new DeviceTopic(deviceId).control();;
		this.heartbeatTopic = DevicesTopic.heartbeat();
		this.statusTopic = mqttTopic.status();
		this.connect();

		log.info("Mock device created! id=" + this.deviceId + " @" + this);

	}

	public Map<String, Object> getChannles() {
		return channles;
	}

	@Override
	public void connect() {
		try {
			super.connect();

			subscribe(controlTopic);
			subscribe(new DevicesTopic().device(deviceId).update());

			startHeartbeat();
			startRandomStatus();

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void shutdown() {
		isShutdown = true;
		this.disconnect();
	}

	// -------------------------------------------------------------

	private DeviceStatusNotification getRandomStatus() {
		DeviceStatusNotification status = new DeviceStatusNotification();
		status.setDeviceId(deviceId);
		status.setChannelValue("A1", getRandomValue(20, 28).toString());
		status.setChannelValue("A2", getRandomValue(1, 3).toString());
		status.setChannelValue("D1", this.channles.get("D1").toString());

		return status;

	}

	private void publishStatus(DeviceStatusNotification deviceStatus) {
		publish(statusTopic, deviceStatus);
	}

	private Integer getRandomValue(int low, int high) {
		Random r = new Random();
		return r.nextInt(high - low) + low;
	}

	private Thread startRandomStatus() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!isShutdown) {
					try {

						DeviceStatusNotification notification = getRandomStatus();
						notification.getChannels().forEach(channle -> {
							channles.put(channle.getName(), channle.getValue());
						});

						publishStatus(notification);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				DeviceStatusNotification status = getRandomStatus();
				status.setOnline(false);
				publishStatus(status);
			}

		});

		t.start();

		return t;
	}

	private Thread startHeartbeat() {
		Thread thread = new Thread(new Runnable() {
			int counter = 0;

			@Override
			public void run() {
				while (!isShutdown && MockDevice.this.isMqttConnected()) {
					try {

						Heartbeat heartBeat = new Heartbeat();
						heartBeat.setDeviceId(deviceId);
						heartBeat.setSequence(++counter);
						heartBeat.setTimestamp(System.currentTimeMillis());

						publish(heartbeatTopic, heartBeat);

						log.debug("Heartbeat published to topic: " + heartbeatTopic + "   " + heartBeat);
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		thread.start();

		return thread;

	}

}