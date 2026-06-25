package rmi_package;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Admin Client for the Video Library System.
 * Connects to the RMI server and launches the main application.
 */
public class AdminClient {
    public static VLSService service;

    public static void connect() {
        try {
            // Change IP to your server's IP address
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (VLSService) registry.lookup("VLS");
            System.out.println("Admin Connected to Server");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to server");
        }
    }

    public static void main(String[] args) {
        connect();
        if (service != null) {
            application.Main.main(args);
        }
    }
}