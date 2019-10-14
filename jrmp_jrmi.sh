mvn package
#server
java -cp target/kuzenko-1.0-SNAPSHOT.jar az1.Server.RmiJrmpServer
#client
java -cp target/kuzenko-1.0-SNAPSHOT.jar az1.Client.GUILauncher -UseJrmpController