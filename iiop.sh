# once until reboot:
#!/usr/bin/env bash
start orbd -ORBInitialPort 1050
# common:
mvn package
# server:
java -cp target/kuzenko-1.0-SNAPSHOT.jar:target/rmi-classes me.az1.dblab.Server.RmiIiopServer
# client:
#!/usr/bin/env bash
java -cp target/kuzenko-1.0-SNAPSHOT.jar:target/rmi-classes az1.Client.GUILauncher -UseIiopController