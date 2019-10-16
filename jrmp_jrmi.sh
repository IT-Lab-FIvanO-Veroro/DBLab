#!/usr/bin/env bash
mvn package
#run server and client
xterm -e ./jrmp_jrmi_server.sh &
(sleep 3 && xterm -e ./jrmp_jrmi_client.sh)
