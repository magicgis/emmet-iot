package com.emmet.iot.core.amqp;

public class SubjectRoutingkey {
	private static final String PREFIX = "view";
	private static final String UPDATE = "update";
	private static final String STATUS = "status";
	private static final String DELIMITER = ".";

	private String subject;
	private String topic;

	public SubjectRoutingkey(String subject) {
		this.subject = subject;
	}

	public SubjectRoutingkey topic(String topic) {
		this.topic = topic;

		return this;
	}

	public String status() {
		return PREFIX + DELIMITER + subject + DELIMITER + topic + DELIMITER + STATUS;
	}

	public String update() {
		return PREFIX + DELIMITER + subject + DELIMITER + topic + DELIMITER + UPDATE;
	}

}