FROM rabbitmq:3-management


RUN rabbitmq-plugins enable --offline rabbitmq_web_stomp
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt

EXPOSE 15674 15674
EXPOSE 61613 61613
EXPOSE 1883 1883



