FROM openjdk:8-jre-alpine

MAINTAINER yefangyi

ENV SERVICE_HOME /usr/moose/service-consumer
ENV PATH ${PATH}:${SERVICE_HOME}

COPY start-service-consumer.sh /tmp/
COPY com.moose.web.service.consumer-1.0.0-SNAPSHOT.jar $SERVICE_HOME/

RUN apk add --no-cache bash \
 && chmod a+x /tmp/*.sh \
 && mv /tmp/start-service-consumer.sh "$SERVICE_HOME"

EXPOSE 8070
WORKDIR $SERVICE_HOME
CMD ["start-service-consumer.sh"]