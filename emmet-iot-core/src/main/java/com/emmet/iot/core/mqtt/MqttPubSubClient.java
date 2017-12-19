package com.emmet.iot.core.mqtt;

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MqttPubSubClient {

	private Log log = LogFactory.getLog(MqttPubSubClient.class);
	private MemoryPersistence persistence = new MemoryPersistence();
	private String brokerUrl = null;
	private String clientId = UUID.randomUUID().toString();
	private final int qos = 2;
	private MqttClient client = null;
	private WatchDog watchDog = new WatchDog();

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	protected String getBrokerUrl() {
		return brokerUrl;
	}

	protected boolean isMqttConnected() {
		return client.isConnected();
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public void connect() throws MqttException {

		client = new MqttClient(brokerUrl, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		client.connect(connOpts);

		client.setCallback(mqttCallback);
		log.info("MQTT broker connected!");

		if (!watchDog.isRunning()) {
			startMqttConnectionWatchDog();
		}
	}

	public void disconnect() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				log.info("Start to disconect from MQTT broker.");
				log.info("Waiting for watchdog stoped...");
				watchDog.disable();

				while (!watchDog.isStoped()) {

				}

				try {
					client.disconnect();
					client.close();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info("Disconnected from MQTT. ClientId:" + clientId);

			}

		});
		t.start();
	

	}

	public void subscribe(String topic) throws MqttException {

		client.subscribe(topic);
		log.debug("Topc :" + topic + " has subscribed.");
	}

	protected void publish(String topic, String messageStr) throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage(messageStr.getBytes());
		message.setQos(qos);
		client.publish(topic, message);
	}

	protected void publish(String topic, Object object) {
		if (!client.isConnected()) {
			try {
				connect();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(object);

			MqttMessage message = new MqttMessage(jsonString.getBytes());
			message.setQos(qos);
			this.publish(topic, message.toString());

			log.debug("Publish message! topic: " + topic + " message: " + jsonString);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void mqttMessageArrived(String topic, MqttMessage msg);

	class WatchDog implements Runnable {

		private boolean running = false;
		private boolean stoped = false;

		public boolean isRunning() {
			return running;
		}

		public boolean isStoped() {
			return stoped;
		}

		public void disable() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {

					Thread.currentThread();
					Thread.sleep(3000);

					if (!client.isConnected()) {
						log.warn("MQTT broker disconnectd! try to reconnect...");
						connect();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
			stoped = true;
			log.info("Watchdog has stoped.");
		}

		public void active() {
			running = true;

		}

	}

	private void startMqttConnectionWatchDog() {
		watchDog.active();
		Thread thread = new Thread(watchDog);
		thread.start();
		log.info("MQTT connection watchdog started.");
	}

	private MqttCallback mqttCallback = new MqttCallback() {

		@Override
		public void connectionLost(Throwable cause) {
			log.warn("Connection lost! " + cause);
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			log.debug("Delivery Complete. token: " + token);
		}

		@Override
		public void messageArrived(String topic, MqttMessage msg) throws Exception {
			mqttMessageArrived(topic, msg);
		}

	};

}
