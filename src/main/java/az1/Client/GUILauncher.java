package az1.Client;

import az1.Client.GUI.MainPanel;
import az1.Common.*;
import az1.Server.ServerController;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
//import javax.rmi.PortableRemoteObject;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public class GUILauncher {
    protected static DatabaseController GetLocalController() {
        return new ServerController();
    }

    protected static DatabaseController GetRmiJrmpController() {
        try {
            return (DatabaseController) Naming.lookup("//localhost/RmiJrmpServer");
        } catch (NotBoundException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return null;
    }

    protected static DatabaseController GetRmiIiopController() {
//        Properties p = System.getProperties();
//        p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
//        p.put("java.naming.provider.url", "iiop://localhost:1050");
//
//        try {
//            Context context = new InitialContext();
//            Object reference = context.lookup("RmiIiopServer");
//            return (DatabaseController) PortableRemoteObject.narrow(reference, DatabaseController.class);
//        } catch (NamingException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }

        return null;
    }



//    protected static DatabaseController GetWebServiceController() {
//        try {
//            URL url = new URL("http://localhost:7777/ws_db");
//            // URL url = new URL("http://dblab-server.herokuapp.com/ws_db?wsdl");
//            QName qname = new QName("http://Common.dblab.az1.me/", "DatabaseControllerService");
//            Service service = Service.create(url, qname);
//            return new DatabaseControllerNullUnescapeAdapter(service.getPort(DatabaseControllerWeb.class));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public static void main(String[] args) {
        System.out.println("Starting gui");
        DatabaseController controller = null;
        for (String arg : args) {
            if (arg.equals("-UseLocalController")) {
                System.out.println("Local controller");
                controller = GetLocalController();
            }
            else if (arg.equals("-UseJrmpController")) {
                System.out.println("JRMP controller");
                controller = GetRmiJrmpController();
            }
            else if (arg.equals("-UseIiopController")) {
                System.out.println("IIOP controller");
                controller = GetRmiIiopController();
            }
        }

        if (controller == null) {
            System.out.println("Controller not found.");
            return;
        }

        final DatabaseController finalController = controller;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Gui");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new MainPanel(new ClientController(finalController)), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
