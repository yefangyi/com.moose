FROM openjdk:8-jre-alpine

MAINTAINER yefangyi

ENV SERVICE_HOME /usr/moose/user-service
ENV PATH ${PATH}:${SERVICE_HOME}

COPY start-user-service.sh /tmp/
COPY com.moose.web.service.user-1.0.0-SNAPSHOT.jar $SERVICE_HOME/

RUN apk add --no-cache bash \
 && chmod a+x /tmp/*.sh \
 && mv /tmp/start-user-service.sh "$SERVICE_HOME"

EXPOSE 8070
WORKDIR $SERVICE_HOME
CMD ["start-user-service.sh"]