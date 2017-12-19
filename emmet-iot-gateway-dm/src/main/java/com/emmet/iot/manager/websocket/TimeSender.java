package com.emmet.iot.manager.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//@Component
public class TimeSender {
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final Log log = LogFactory.getLog(TimeSender.class);

	@Autowired
	private SimpMessagingTemplate broker;

	@Autowired
	public TimeSender(final SimpMessagingTemplate broker) {
		this.broker = broker;
	}

	@Scheduled(fixedRate = 1000)
	public void run() {
		String time = LocalTime.now().format(TIME_FORMAT);

		log.debug("Time broadcast: " + time);
		broker.convertAndSend("/topic/time", time);
	}
}
