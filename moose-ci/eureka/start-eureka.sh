#!/bin/bash

if [[ -z "$SECURITY_BASIC_ENABLED" ]]; then
    if [[ -z "$SECURITY_USER_NAME" || -z "$SECURITY_USER_PASSWORD" ]]; then
        export SECURITY_BASIC_ENABLED=false
    else
        export SECURITY_BASIC_ENABLED=true
    fi
fi
if [[ -z "$EUREKA_INSTANCE_HOSTNAME" ]]; then
    export EUREKA_INSTANCE_HOSTNAME=localhost
fi
if [[ -z "$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE" ]]; then
    export EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:$SERVER_PORT/eureka
fi

if [ "$SECURITY_BASIC_ENABLED" == "true" ]; then
    java -Dsecurity.basic.enabled=$SECURITY_BASIC_ENABLED \
        -Dsecurity.user.name=$SECURITY_USER_NAME -Dsecurity.user.password=$SECURITY_USER_PASSWORD \
        -Deureka.instance.hostname=$EUREKA_INSTANCE_HOSTNAME \
        -Deureka.client.service-url.defaultZone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE \
        -jar com.moose.eureka.server-1.0.0-SNAPSHOT.jar
else
    java -Dsecurity.basic.enabled=$SECURITY_BASIC_ENABLED \
        -Deureka.instance.hostname=$EUREKA_INSTANCE_HOSTNAME \
        -Deureka.client.service-url.defaultZone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE \
        -jar com.moose.eureka.server-1.0.0-SNAPSHOT.jar
fi
