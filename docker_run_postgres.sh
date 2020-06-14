#!bin/bash
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v /home/phil/docker/volumes/postgres:/var/lib/postgresql/data postgres

