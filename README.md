# SOULSwap
Serves SOUL (Sound Language) Patches

to run postgresql and soulswap in docker container

`$ ./mvnw spring-boot:build-image -Pproduction`

until something like that:

```
[INFO]     [creator]     Adding 5/5 app layer(s)
[INFO]     [creator]     Adding layer 'config'
[INFO]     [creator]     *** Images (68ee69345f96):
[INFO]     [creator]           docker.io/library/soulswap:0.0.1-SNAPSHOT
[INFO]
[INFO] Successfully built image 'docker.io/library/soulswap:0.0.1-SNAPSHOT'
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
app_1  | 2020-06-18 16:53:02.817  INFO 1 --- [           main] i.h.soulswap.SOULSwapApplication         : Started SOULSwapApplication in 26.931 seconds (JVM running for 28.557)
app_1  | SOULSwap Application started!
````

to input some testdata connect to postgresql and run the sqls in input_testdata.sql

wsdl gets generated at: `http://<server>:<port>/ws/SOULSwapWsdl.wsdl`

test request:

`$ curl --header "content-type: text/xml" -d @docs/request.xml http://<server>:<port>/ws`
