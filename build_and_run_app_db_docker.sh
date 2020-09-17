#!/usr/bin/env bash
./mvnw spring-boot:build-image -Pproduction && docker-compose build && docker-compose up -d \
  && echo "visit http://localhost:8080 (might have to wait a minute or two for the app to fully startup)" \
  || echo "error"

