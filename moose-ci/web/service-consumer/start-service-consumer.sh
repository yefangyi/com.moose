#!/bin/bash

java -Deureka.client.serviceUrl.defaultZone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE \
    -jar com.moose.web.service.consumer-1.0.0-SNAPSHOT.jar
