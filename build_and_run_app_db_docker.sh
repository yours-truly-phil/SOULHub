#!/usr/bin/env bash
mvn spring-boot:build-image -Pproduction
docker-compose build
docker-compose up
