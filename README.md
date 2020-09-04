# SOULHub

Web-application that serves the SOUL (Sound Language) community as a SOULPatch Exchange.

For users to edit and publish their SOULPatches and easily find public SOULPatches created by others.

The open API invites other systems, like IDE Plugins specialized in SOUL, 
 or a discord bot (in the planning ;)) to connect to SOULHub, so sticking soulpatches together
 gets a bit closer to playing LEGO, where the right pieces just fall into your lap.

#### the fastest way to build and run SOULHub as Webapp in Docker

## Prerequisites

* maven
* jdk 14
* docker
* docker-compose

## build

run

- `./build_and_run_app_db_docker.sh`

or

- `build_and_run_app_db_docker.cmd`

or manually

1. `mvn spring-boot:build-image -Pproduction`
2. `docker-compose build && docker-compose up`

## finally

goto localhost:8080

## about this build

This takes a long time. If you're a c++ developer: This takes basically no time.

Compiles the frontend to javascript and runs two docker container
for the SOULHub app and postgres. 

Postgres is spun up with a default postgres db for the
user postgres with the password postgres. Make sure it's not accessible from the outside (Port 5432).

If no database exists (initial launch) the two sql scripts ./sql/10_schema.sql
and ./sql/20_data.sql get executed. This creates the tables and inserts test-data and
test-users.

Login with the user 'dbuser1' and password 'password' in SOULHub (localhost:8080).


###### dev (deprecated)

postgres has to be running (any recent postgres docker image).
It can be run by ./docker_run_postgres.sh (careful -> paths)

`docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v [YOUR HOMEDIR]/docker/volumes/postgres:/var/lib/postgresql/data postgres`

To get some data into the system, execute the sql scripts (./sql/10_schema.sql and then ./sql/20_data.sql)
against the postgres database.

Then run or debug the SOULHubApplication spring boot application.

Once the application is running, visit localhost:8080.

The ./sql/20_data.sql inserted two users, a user with role user (username: dbuser1 password: password)
and a user with role user and admin (username: dbadmin1 password: password)

###### production (deprecated)

to run postgresql and soulhub in docker container

`$ ./mvnw spring-boot:build-image -Pproduction`

until something like that:

```
[INFO]     [creator]     Adding 5/5 app layer(s)
[INFO]     [creator]     Adding layer 'config'
[INFO]     [creator]     *** Images (68ee69345f96):
[INFO]     [creator]           docker.io/library/soulhub:0.0.1-SNAPSHOT
[INFO]
[INFO] Successfully built image 'docker.io/library/soulhub:0.0.1-SNAPSHOT'
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  05:14 min
[INFO] Finished at: 2020-06-18T18:48:28+02:00
[INFO] ------------------------------------------------------------------------
```

and then:

`$ docker-compose up`

until this:

```
app_1  | 2020-06-18 16:53:01.773  INFO 1 --- [           main] DeferredRepositoryInitializationListener : Spring Data repositories initialized!
app_1  | 2020-06-18 16:53:02.817  INFO 1 --- [           main] i.h.soulhub.SOULHubApplication         : Started SOULHubApplication in 26.931 seconds (JVM running for 28.557)
app_1  | SOULHub Application started!
````

to input some testdata connect to postgresql and run the sql script ./sql/20_data.sql

wsdl gets generated at: `http://<server>:<port>/ws/SOULHubWsdl.wsdl`

test request:

`$ curl --header "content-type: text/xml" -d @docs/request.xml http://<server>:<port>/ws`
