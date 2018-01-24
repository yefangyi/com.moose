FROM openjdk:8-jre-alpine

MAINTAINER yefangyi

ENV EUREKA_HOME /usr/eureka
ENV PATH ${PATH}:${EUREKA_HOME}

COPY start-eureka.sh /tmp/
COPY com.moose.eureka.server-1.0.0-SNAPSHOT.jar $EUREKA_HOME/

RUN apk add --no-cache bash \
 && chmod a+x /tmp/*.sh \
 && mv /tmp/start-eureka.sh "$EUREKA_HOME"

EXPOSE 8000
WORKDIR $EUREKA_HOME
CMD ["start-eureka.sh"]