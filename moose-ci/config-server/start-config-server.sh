#!/bin/bash

java -Dspring.cloud.stream.kafka.binder.brokers=$KAFKA_BROKERS \
    -Dspring.cloud.stream.kafka.binder.zkNodes=$ZOOKEEPER_NODES \
    -Deureka.client.serviceUrl.defaultZone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE \
    -jar com.moose.config.server-1.0.0-SNAPSHOT.jar
