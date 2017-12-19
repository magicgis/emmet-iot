package com.emmet.iot.manager;

import java.util.Date;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@RabbitListener(queues = "device-abc")
public class DeviceManagerApplication {
	@Bean
	public Queue fooQueue() {
		return new Queue("device-abc");
	}

	 @Bean
	    TopicExchange exchange() {
	        return new TopicExchange("device");
	    }

	    @Bean
	    Binding binding(Queue queue, TopicExchange exchange) {
	        return BindingBuilder.bind(queue).to(exchange).with("device-abc");
	    }
	
	@RabbitHandler
	public void process(@Payload String foo) {
		System.out.println(new Date() + ": " + foo);
	}
	
	public static void main(String[] args) {

		SpringApplication.run(DeviceManagerApplication.class, args);

	}
}
