package com.emmet.iot.core.mqtt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;

import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.mqtt.util.MqttMessagePublisher;
import com.emmet.iot.core.mqtt.util.MqttSubscriptionListener;


public class MqttPubSubClientTest {

	private static final String brokerUrl = "tcp://iot.eclipse.org:1883";

	MqttMessagePublisher mqttMessagePublisher = new MqttMessagePublisher();

	MqttSubscriptionListener mqttSubscribeListener = new MqttSubscriptionListener();
	
	private static final String SUB_TOPIC = "SUB_TOPIC";
	private static final String PUB_TOPIC = "PUB_TOPIC";
	private static final String SUB_MSG = "SUB_MSG";
	private static final String PUB_MSG = "PUB_MSG";

	@Test
	public void test() throws MqttException, InterruptedException {
		
		
		mqttSubscribeListener.subscribe(PUB_TOPIC);

		MqttPubSubClient mqttPubSubClient = new MqttPubSubClient() {
			@Override
			public void mqttMessageArrived(String topic, MqttMessage msg) {
				System.out.println(msg);
				assertThat(topic, equalTo(SUB_TOPIC));
				assertThat(msg.toString(), equalTo(SUB_MSG));
			}

			@Override
			public void connect() throws MqttException {
				super.connect();
				subscribe(SUB_TOPIC);
				super.publish(PUB_TOPIC, PUB_MSG);
			}
		};

		mqttPubSubClient.setBrokerUrl(brokerUrl);
		mqttPubSubClient.connect();
		mqttMessagePublisher.publish(SUB_TOPIC, SUB_MSG);
		
		Thread.currentThread();
		Thread.sleep(3000);

		
		
	}
}
