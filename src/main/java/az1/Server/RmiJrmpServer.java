package az1.Server;

import az1.Common.DatabaseController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RmiJrmpServer {
    public static void main(String[] args) {
        System.out.println("RMI JRMP server starting");

        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            DatabaseController controller = new ServerController();
            controller = (DatabaseController) UnicastRemoteObject.exportObject(controller, 0);
            Naming.rebind("//localhost/RmiJrmpServer", controller);
        } catch (RemoteException e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(-1);
        } catch (MalformedURLException e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Started");
    }
}
