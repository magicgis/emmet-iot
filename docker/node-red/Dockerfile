FROM node:4

RUN npm install -g --unsafe-perm node-red
RUN npm install -g node-red-contrib-amqp
RUN npm install -g node-red-node-stomp
RUN npm install request

COPY ./node-red-entrypoint.sh /usr/local/bin/node-red-entrypoint.sh

RUN ls -l /usr/local/bin/

ENTRYPOINT ["node-red-entrypoint.sh"]
EXPOSE 1880
