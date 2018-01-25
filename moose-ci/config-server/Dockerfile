FROM openjdk:8-jre-alpine

MAINTAINER yefangyi

ENV CONFIG_HOME /usr/spring-cloud-config
ENV PATH ${PATH}:${CONFIG_HOME}

COPY start-config-server.sh /tmp/
COPY com.moose.config.server-1.0.0-SNAPSHOT.jar $CONFIG_HOME/

RUN apk add --no-cache bash \
 && chmod a+x /tmp/*.sh \
 && mv /tmp/start-config-server.sh "$CONFIG_HOME"

EXPOSE 8888
WORKDIR $CONFIG_HOME
CMD ["start-config-server.sh"]