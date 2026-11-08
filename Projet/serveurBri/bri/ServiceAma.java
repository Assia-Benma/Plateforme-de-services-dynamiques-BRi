package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Session d'un amateur connecté sur PORT_AMA.
 * Rôle : afficher les services démarrés, lancer celui choisi.
 */
public class ServiceAma implements Service {

    private final Socket client;

    public ServiceAma(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (Socket socket = client;
             BufferedReader entree = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(),
                             StandardCharsets.UTF_8));
             PrintWriter sortie = new PrintWriter(
                     new java.io.OutputStreamWriter(
                             socket.getOutputStream(),
                             StandardCharsets.UTF_8), true)) {
            sortie.println(ServiceRegistry.toStringue());
            sortie.println("NUMERO_SERVICE");

            String choix = entree.readLine();
            if (choix == null || choix.trim().isEmpty()) {
                sortie.println("REFUS : Le numéro de service est obligatoire");
                return;
            }

            int numero;
            try {
                numero = Integer.parseInt(choix.trim()) - 1;
            } catch (NumberFormatException e) {
                sortie.println("REFUS : Le numéro de service est invalide");
                return;
            }

            Class<? extends Service> classeService;
            try {
                classeService = ServiceRegistry.getServiceClass(numero);
            } catch (IndexOutOfBoundsException e) {
                sortie.println("REFUS : " + e.getMessage());
                return;
            }

            sortie.println("SERVICE_SELECTIONNE##" + classeService.getName());
            lancerService(classeService);
        } catch (IOException e) {
            System.err.println("Session amateur interrompue : "
                    + e.getMessage());
        }
    }

    private void lancerService(Class<? extends Service> classeService) {
        try {
            Service service = classeService.getConstructor(Socket.class)
                    .newInstance(client);
            service.run();
        } catch (ReflectiveOperationException | ClassCastException e) {
            System.err.println("Impossible de lancer le service "
                    + classeService.getName() + " : " + e.getMessage());
            fermer();
        }
    }

    private void fermer() {
        try {
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            System.err.println("Impossible de fermer la socket : "
                    + e.getMessage());
        }
    }
}
