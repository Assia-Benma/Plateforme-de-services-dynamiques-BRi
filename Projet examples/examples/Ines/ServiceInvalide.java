package Ines;

import bri.Service;
import java.net.Socket;

public class ServiceInvalide implements Service {

    private Socket client;

    public ServiceInvalide(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
    }

    public static String toStringue() {
        return "Service invalide";
    }
}
