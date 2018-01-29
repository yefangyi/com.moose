#!/bin/bash

java -Deureka.client.serviceUrl.defaultZone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE \
    -jar com.moose.web.service.user-1.0.0-SNAPSHOT.jar
