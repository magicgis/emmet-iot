package com.emmet.iot.manager.observer;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.emmet.iot.core.amqp.DeviceRoutingkey;
import com.emmet.iot.core.config.Constant;
import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.mqtt.DevicesTopic;
import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.observer.BaseDeviceStatusObserver;
import com.emmet.iot.core.util.JsonHelper;

@Component
public class ManagedDevicesStatusPublisher extends BaseDeviceStatusObserver {
	private static final Log log = LogFactory.getLog(ManagedDevicesStatusPublisher.class);

	@Autowired
	private AmqpTemplate rabbitTemplate;

	final static String deviceStatusTopic = "device-status";
	final static String routingKeyPrefix = "device.";

	@Bean
    TopicExchange exchange() {
        return new TopicExchange(deviceStatusTopic);
    }
	
	@PostConstruct
	public void connect() {

	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void handle(DeviceStatusNotification status) {
		
		String deviceId = status.getDeviceId();
		status.getChannels().forEach(channel -> {
			channel.setOnline(status.isOnline());
			String message = JsonHelper.ObjectToJsonString(channel);
			String channelName = channel.getName();
			String routeKey = new DeviceRoutingkey(deviceId).cannel(channelName).status();
					//routingKeyPrefix + deviceId + "." + channelName + ".get";
			log.debug("Send data to RabbitMq, routing key: "+ routeKey + " message: " + message);
			this.rabbitTemplate.convertAndSend(deviceStatusTopic, routeKey, message);
		});

	}

}
