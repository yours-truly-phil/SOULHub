# SOULHub

Serves SOUL (Sound Language) Patches to SOUL IDE Plugins that
allow the developer to quickly find, switch and try out patches from the community
in their SOUL graph without having to leave the IDE and all sorts of awesome stuff TBD. 

#### how to build (reminder to myself)

docker-compose.yml and docker_run_postgres.sh still contain hardcoded paths 
that need to fit the given environment.

##### dev

postgres has to be running (any recent postgres docker image).
It can be run by ./docker_run_postgres.sh (careful -> paths)

And then like any spring-boot project with the IDE (or without) 
the maven profile spring-boot:run.

##### production

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

to input some testdata connect to postgresql and run the sqls in data.sql

wsdl gets generated at: `http://<server>:<port>/ws/SOULHubWsdl.wsdl`

test request:

`$ curl --header "content-type: text/xml" -d @docs/request.xml http://<server>:<port>/ws`
