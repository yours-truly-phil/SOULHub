#!/bin/bash
./mvnw clean && ./mvnw install
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build -t horrorshow.io/soulswap/soulswap-web-server .
