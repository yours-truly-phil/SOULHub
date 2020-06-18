#!/bin/bash
#./mvnw clean && ./mvnw install
#mkdir -p target/dependency && (cd target/dependency || exit; jar -xf ../*.jar)
./mvnw spring-boot:build-image -Pproduction
#docker build -t horrorshow.io/soulswap/soulswap-web-server .
