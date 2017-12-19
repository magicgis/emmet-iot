package com.emmet.iot.manager.amqp;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.emmet.iot.core.model.ChannelUpdateRequest;
import com.emmet.iot.core.util.JsonHelper;
import com.emmet.iot.ds.shadow.DeviceShadow;
import com.emmet.iot.manager.collection.DeviceCollection;
import com.emmet.iot.manager.exception.DeviceNotFoundException;

@Component
@RabbitListener(queues = "device_update")
public class TopicUpdateRequestListener {

	@Autowired
	DeviceCollection deviceCollection;

	@RabbitHandler
	public void process(@Payload byte[] payload) throws DeviceNotFoundException, MqttPersistenceException, MqttException {
		System.out.println(new Date() + ": " + new String(payload));
		ChannelUpdateRequest request = JsonHelper.JsonStringToObject(new String(payload), ChannelUpdateRequest.class);
		System.out.println(request);
		
		DeviceShadow device = deviceCollection.getDevice(request.getDeviceId());
		device.updateDevice(request);
		
	}
}
