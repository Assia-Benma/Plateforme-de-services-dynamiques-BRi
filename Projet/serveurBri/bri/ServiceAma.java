import java.net.Socket;

/**
 * Session d'un amateur connecté sur PORT_AMA.
 * Rôle : afficher les services démarrés, lancer celui choisi.
 */
public class ServiceAma implements Service {

//placeholder
    private static final String NOM_SERVICE = "exemple.ServiceInversion";
    private final Socket client;

    public ServiceAma(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // 1. Retrouver la classe à partir de son nom (chargement dynamique)
            Class<? extends Service> classeService =
                    Class.forName(NOM_SERVICE).asSubclass(Service.class);
 
            // 2. Créer une instance en lui donnant la socket du client
            Service service = classeService
                    .getConstructor(Socket.class)
                    .newInstance(client);
 
            // 3. Lancer le service dans CE thread : le dialogue est maintenant
            //    entre le client et ServiceInversion. Quand il se termine,
            //    il a fermé la socket lui-même.
            service.run();
 
        } catch (ReflectiveOperationException | ClassCastException e) {
            System.err.println("Impossible de lancer le service " + NOM_SERVICE + " : " + e);
            fermer();
        }
    }
    
    private void fermer() {
        try {
            client.close();
        } catch (IOException e) {
            // rien à faire
        }
    }

}

