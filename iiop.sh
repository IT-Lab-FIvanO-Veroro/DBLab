#!/usr/bin/env bash
# once until reboot:
# start orbd -ORBInitialPort 1050
# common:
mvn package
# server:
java -cp target/kuzenko-1.0-SNAPSHOT.jar:target/rmi-classes az1.Server.RmiIiopServer
# client:
java -cp target/kuzenko-1.0-SNAPSHOT.jar:target/rmi-classes az1.Client.GUILauncher -UseIiopController
