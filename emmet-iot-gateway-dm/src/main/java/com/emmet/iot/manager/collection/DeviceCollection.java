package com.emmet.iot.manager.collection;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emmet.iot.core.config.Constant;
import com.emmet.iot.core.model.DeviceStatusListener;
import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.model.Heartbeat;
import com.emmet.iot.core.model.RequestDeviceStatusCommand;
import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.mqtt.DeviceTopic;
import com.emmet.iot.core.mqtt.DevicesTopic;
import com.emmet.iot.core.observer.DeviceStatusObserver;
import com.emmet.iot.core.observer.DeviceStatusSubject;
import com.emmet.iot.core.util.JsonHelper;
import com.emmet.iot.ds.shadow.DeviceShadow;
import com.emmet.iot.manager.exception.DeviceNotFoundException;
import com.emmet.iot.manager.observer.DeviceStatusWebsocket;
import com.emmet.iot.manager.observer.ManagedDevicesStatusPublisher;

@Service
public class DeviceCollection extends MqttPubSubClient implements DeviceStatusListener, DeviceStatusSubject {

	@Autowired
	DeviceStatusWebsocket deviceStatusWebsocket;

	@Autowired
	ManagedDevicesStatusPublisher managedDevicesStatusPublisher;

	// private DevicesTopic mqttTopic = new DevicesTopic();

	private static final Log log = LogFactory.getLog(DeviceCollection.class);

	@Value("${gateway.mqtt.broker-url}")
	private String mqttBrokerUrl;

	private List<DeviceShadow> devices = new ArrayList<>();

	public List<DeviceShadow> getAllDevices() {
		return devices;
	}

	public DeviceShadow getDevice(String deviceId) throws DeviceNotFoundException {

		for (DeviceShadow device : devices) {
			if (deviceId.equals(device.getDeviceId())) {
				return device;
			}
		}
		throw new DeviceNotFoundException(deviceId);
	}

	@Override
	@PostConstruct
	public void connect() throws MqttException {

		addObserver(deviceStatusWebsocket);
		addObserver(managedDevicesStatusPublisher);

		this.setBrokerUrl(mqttBrokerUrl);

		super.connect();
		log.info("<Device Manager> has conncted to MQTT Broker.");

		String heartbeatTopic = DevicesTopic.heartbeat();
		subscribe(heartbeatTopic);
		
		Thread t = new Thread(new HeartbeatWatchdog());
		t.start();
	}

	@Override
	public void mqttMessageArrived(String topic, MqttMessage msg) {
		log.debug("<Device Manager> Message arrived. Topic:" + topic + " messge:" + msg);

		if (topic.startsWith(DevicesTopic.heartbeat())) {
			log.debug("<Device Manager> Heartbeat received:" + msg);
			Heartbeat heartbeat = JsonHelper.JsonStringToObject(new String(msg.getPayload()), Heartbeat.class);
			this.onHeartbeatReceived(heartbeat);
		}

	}

	@PreDestroy
	public void shutdown() {

		watchDogisRunning = false;
		
		log.info("########## Shuting down device shadows...##########");
		for (DeviceShadow device : devices) {
			log.info("Shuting down device: " + device.getDeviceId());
			device.disconnect();
		}
		log.info("########All device shadows had shutdown. ##########");

		log.info("Shuting down device manager.");
		disconnect();
		log.info("Device manager disconnected from MQTT broker.");

		for (DeviceStatusObserver observer : deviceStatusObservers) {
			log.info("Shuting down device status observer..." + observer.getClass());
			observer.stop();
		}
	}

	private void onHeartbeatReceived(Heartbeat heartbeat) {
		String deviceId = heartbeat.getDeviceId();
		DeviceShadow deviceShadow = null;
		if (!isDeviceShadowManaged(deviceId)) {
			
			try {
				deviceShadow = new DeviceShadow(deviceId, mqttBrokerUrl, this);
				deviceShadow.connect();
				devices.add(deviceShadow);
				log.info("<Device Manager> " + deviceId + ": device shadow has created");
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.sendDeviceStatusReportRequest(deviceId);
			
		} else {
			try {
				deviceShadow = this.getDevice(deviceId);
				if (!deviceShadow.isOnLine()) {
					this.sendDeviceStatusReportRequest(deviceId);
				}

			} catch (DeviceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		deviceShadow.updateHeartbeatTime(System.currentTimeMillis());
	}

	private List<DeviceStatusObserver> deviceStatusObservers = new ArrayList<>();

	@Override
	public void onDeviceStatusChange(DeviceStatusNotification status) {
		updateStatus(status);
	}

	@Override
	public void addObserver(DeviceStatusObserver observer) {
		deviceStatusObservers.add(observer);

	}

	@Override
	public void updateStatus(DeviceStatusNotification status) {
		for (DeviceStatusObserver observer : deviceStatusObservers) {
			observer.onDeviceStatusChange(status);
		}
	}

	// --------------------------------------------------------------------------

	private void sendDeviceStatusReportRequest(String deviceId) {
		String deviceControlTopic = new DeviceTopic(deviceId).control();
		RequestDeviceStatusCommand command = new RequestDeviceStatusCommand();

		this.publish(deviceControlTopic, command);
		log.info("<Device Manager> Reporting device status request has published to: (" + deviceId + ")");
	}

	private boolean isDeviceShadowManaged(String deviceID) {
		for (DeviceShadow deviceShadow : devices) {
			if (deviceShadow.getDeviceId().equals(deviceID)) {
				return true;
			}
		}
		return false;
	}

	private boolean watchDogisRunning = true;

	class HeartbeatWatchdog implements Runnable {
		@Override
		public void run() {
		
			while (watchDogisRunning) {
				log.debug("------------------HeartbeatWatchdog------------------------");
				Thread.currentThread();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				devices.forEach(device -> {
					if (!device.isOnLine()) {
						log.debug(device.getDeviceId() + " is offline");
						DeviceStatusNotification status = new DeviceStatusNotification();
						status.setOnline(false);
						status.setDeviceId(device.getDeviceId());
						device.getCurrentStatus().getDataChannels().forEach((name,value)->{
							status.setChannelValue(name, "");
						});
						
						managedDevicesStatusPublisher.handle(status);
					}
				});

			}
			log.info("Heartbeat Watchdog was stoped.");
		}
	}

}
