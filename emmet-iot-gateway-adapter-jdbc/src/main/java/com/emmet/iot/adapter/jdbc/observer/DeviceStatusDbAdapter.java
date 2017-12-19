package com.emmet.iot.adapter.jdbc.observer;

import java.sql.Date;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emmet.iot.adapter.jdbc.entity.ChannelValueEntity;
import com.emmet.iot.adapter.jdbc.entity.DeviceStatusLogEntity;
import com.emmet.iot.adapter.jdbc.repository.DeviceStatusRepository;
import com.emmet.iot.core.config.Constant;
import com.emmet.iot.core.model.DeviceStatusNotification;
import com.emmet.iot.core.mqtt.DevicesTopic;
import com.emmet.iot.core.mqtt.MqttPubSubClient;
import com.emmet.iot.core.observer.BaseDeviceStatusObserver;
import com.emmet.iot.core.util.JsonHelper;

@Service
public class DeviceStatusDbAdapter {
	private static final Log log = LogFactory.getLog(DeviceStatusDbAdapter.class);

	@Value("${gateway.mqtt.broker-url}")
	private String mqttBrokerUrl;

	@Autowired
	private DeviceStatusRepository deviceStatusRepository;

	private MqttClient mqttCient;

	@PostConstruct
	public void init() throws MqttException {
		mqttCient = new MqttClient();
		mqttCient.setBrokerUrl(mqttBrokerUrl);
		mqttCient.connect();
		log.info("<DeviceStatusObserver> has cnnected to MQTT broker.");
		
		mqttCient.subscribe(new DevicesTopic().status());

	}

	@PreDestroy
	public void shutdown() {
		log.info("<DeviceStatusDbAdapter> Start to shutdown...");
		mqttCient.disconnect();

	}

	class MqttClient extends MqttPubSubClient {

		@Override
		public void mqttMessageArrived(String topic, MqttMessage msg) {
			DeviceStatusNotification status = JsonHelper.JsonStringToObject(new String(msg.getPayload()),
					DeviceStatusNotification.class);
			System.out.println("---->" + status);

			DeviceStatusLogEntity entity = new DeviceStatusLogEntity();

			entity.setDeviceId(status.getDeviceId());
			entity.setOnline(status.isOnline());

			Calendar c = Calendar.getInstance();

			entity.setTime(c);

			status.getChannels().forEach(channel -> {
				ChannelValueEntity channelEntity = new ChannelValueEntity();
				channelEntity.setChannelName(channel.getName());
				channelEntity.setValue(channel.getValue().toString());
				Calendar calendar = Calendar.getInstance();
			    calendar.setTimeInMillis(channel.getLastUdateTime());
				channelEntity.setLastUpdateTime(calendar);
				entity.getChannels().add(channelEntity);
			});

			deviceStatusRepository.save(entity);

		}
	}

}
