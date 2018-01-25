FROM openjdk:8-jre-alpine

MAINTAINER yefangyi

ENV CONFIG_HOME /usr/spring-cloud-config-client
ENV PATH ${PATH}:${CONFIG_HOME}

COPY start-config-client.sh /tmp/
COPY com.moose.config.client-1.0.0-SNAPSHOT.jar $CONFIG_HOME/

RUN apk add --no-cache bash \
 && chmod a+x /tmp/*.sh \
 && mv /tmp/start-config-client.sh "$CONFIG_HOME"

EXPOSE 8881
WORKDIR $CONFIG_HOME
CMD ["start-config-client.sh"]