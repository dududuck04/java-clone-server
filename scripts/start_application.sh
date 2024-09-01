#!/usr/bin/env bash

cd /service
nohup java -jar /service/carrot-clone-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev > /dev/null 2>&1 &