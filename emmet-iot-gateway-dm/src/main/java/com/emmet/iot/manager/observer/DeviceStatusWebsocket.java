package com.emmet.iot.manager.observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.observer.BaseDeviceStatusObserver;
import com.emmet.iot.core.util.JsonHelper;

@Component
public class DeviceStatusWebsocket extends BaseDeviceStatusObserver {
	private static final Log log = LogFactory.getLog(DeviceStatusWebsocket.class);

	@Autowired
	private SimpMessagingTemplate broker;

	
	@Override
	public void handle(DeviceStatusNotification status) {
		String topic = "/topic/" + status.getDeviceId();
		String payload = JsonHelper.ObjectToJsonString(status);
		log.debug("<Websocket> Publish status by Web socket, topic: " + topic + " payload: " + payload);
		broker.convertAndSend(topic, payload);
	}
	
	
}
