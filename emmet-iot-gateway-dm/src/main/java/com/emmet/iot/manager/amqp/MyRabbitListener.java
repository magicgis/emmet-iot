package com.emmet.iot.manager.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		System.out.println("Listener received message = " + new String(message.getBody()));
	}
}
