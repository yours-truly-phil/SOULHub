#!/bin/bash
docker run \
  -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" \
  -p 8080:8080 \
  -p 5005:5005 \
  -e "SPRING_PROFILES_ACTIVE=dev" \
  -t horrorshow.io/soulswap/soulswap-web-server
