# SOULSwap
Serves SOUL (Sound Language) Patches

to test

`$ ./build_for_docker.sh`

`$ ./run_docker_debug_dev.sh`

wsdl gets generated at: `http://<server>:<port>/ws/SOULSwapWsdl.wsdl`

test request:

`$ curl --header "content-type: text/xml" -d @docs/request.xml http://<server>:<port>/ws`
