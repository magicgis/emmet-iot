package com.emmet.iot.topic.manager;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class TopicManagerApplication {
	

	final static String queueName = "iot-topic-status-all";

	@Bean
	Queue queue() {
		return new Queue(queueName, false, false, true);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("iot-topic-status");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("bathroom.temperature");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(new String[] {queueName});
		container.setMessageListener(new MyRabbitListener());
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");
		return adapter;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(TopicManagerApplication.class, args);
		
	}
	
	public class MyRabbitListener implements MessageListener {
		@Override
		public void onMessage(Message message) {
			System.out.println("Listener received message = " + message.getMessageProperties().getReceivedRoutingKey() + new String(message.getBody()));
		}
	}
	

}
