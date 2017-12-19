package com.emmet.iot.core.mqtt.util;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttMessagePublisher {
	private Log log = LogFactory.getLog(MqttMessagePublisher.class);

	private static final String brokerUrl = "tcp://iot.eclipse.org:1883";

	private String clientId = "PUBLISER-" + UUID.randomUUID().toString();

	private MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient client = null;

	private int qos = 2;

	public MqttMessagePublisher() {
		try {
			client = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();

			connOpts.setCleanSession(true);
			client.connect(connOpts);
			log.info("MQTT broker connected!");
		}
		catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publish(String topic, String msgStr) {
		MqttMessage message = new MqttMessage(msgStr.getBytes());
		message.setQos(qos);
		try {
			client.publish(topic, message);
			log.info("Message published by messagePublisher:" + msgStr);
		}
		catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
