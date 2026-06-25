package rmi_package;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI Server for the Video Library System.
 * Hosts the remote service and binds it to the registry.
 */
public class Server {
    public static void main(String[] args) {
        try {
            VLSService service = new VLSServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("VLS", service);
            System.out.println("VLS Server Running...");
            System.out.println("Server ready on port 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}