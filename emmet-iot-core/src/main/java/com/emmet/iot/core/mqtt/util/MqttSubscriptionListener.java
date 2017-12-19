package com.emmet.iot.core.mqtt.util;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttSubscriptionListener implements MqttCallback {

	private Log log = LogFactory.getLog(MqttSubscriptionListener.class);

	private static final String brokerUrl = "tcp://iot.eclipse.org:1883";
	
	private String clientId = "LITENER-" + UUID.randomUUID().toString();
	
	private MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient client = null;


	public MqttSubscriptionListener() {
		try {
			client = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions options = new MqttConnectOptions();
			client.connect(options);
			log.info("MQTT broker connected!");
			client.setCallback(this);
		} catch (MqttException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void subscribe(String topic) {
		try {
			this.client.subscribe(topic);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		log.error("Connection to " + brokerUrl + " lost!" + cause);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		System.out.println("Id:\t" + message.getId() + "  Topic:\t" + topic + "  Message:\t" + new String(message.getPayload())
				+ "  QoS:\t" + message.getQos());
	}

}
