package bri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Serveur TCP générique : écoute sur un port et, pour chaque client qui se connecte,
 * crée une session (une instance de la classe de service donnée) dans un nouveau Thread.
 *
 * Le même code sert pour le port programmeur ET le port amateur :
 * seule la classe de session change (ServiceProg ou ServiceAma).
 */
public final class ServeurBRi implements Runnable {

    private final ServerSocket listenSocket;
    private final Class<? extends Service> classeSession;

    /**
     * Crée un serveur qui fabriquera une session pour chaque connexion.
     *
     * @param port port TCP d'écoute
     * @param classeSession classe possédant un constructeur public Socket
     */
    public ServeurBRi(int port, Class<? extends Service> classeSession)
            throws IOException {
        if (classeSession == null) {
            throw new IllegalArgumentException("La classe de session est obligatoire");
        }
        this.listenSocket = new ServerSocket(port);
        this.classeSession = classeSession;
    }

    /**
     * Accepte les connexions jusqu'à la fermeture du serveur.
     *
     * Une erreur de création de session ne ferme pas le serveur : seule la
     * connexion concernée est refusée.
     */
    @Override
    public void run() {
        try {
            while (!listenSocket.isClosed()) {
                accepterConnexion();
            }
        } catch (IOException e) {
            if (!listenSocket.isClosed()) {
                System.err.println("Erreur du serveur BRi : " + e.getMessage());
            }
        } finally {
            fermer();
        }
    }

    private void accepterConnexion() throws IOException {
        Socket client = listenSocket.accept();
        try {
            Service session = classeSession
                    .getConstructor(Socket.class)
                    .newInstance(client);

            Thread thread = new Thread(session,
                    "bri-session-" + client.getRemoteSocketAddress());
            thread.start();
        } catch (ReflectiveOperationException | SecurityException e) {
            fermerConnexion(client);
            if (e instanceof InvocationTargetException) {
                InvocationTargetException invocation =
                        (InvocationTargetException) e;
                System.err.println("Le constructeur de la session a échoué : "
                        + invocation.getCause());
            } else {
                System.err.println("Impossible de créer la session : " + e);
            }
        }
    }

    /**
     * Lance la boucle d'écoute dans son propre thread.
     */
    public void lancer() {
        Thread thread = new Thread(this, "bri-serveur-" + listenSocket.getLocalPort());
        thread.start();
    }

    /**
     * Ferme le port d'écoute. Cela débloque également un accept() en cours.
     */
    public void fermer() {
        if (!listenSocket.isClosed()) {
            try {
                listenSocket.close();
            } catch (IOException e) {
                System.err.println("Impossible de fermer le serveur BRi : "
                        + e.getMessage());
            }
        }
    }

    private static void fermerConnexion(Socket client) {
        try {
            client.close();
        } catch (IOException e) {
            System.err.println("Impossible de fermer la connexion cliente : "
                    + e.getMessage());
        }
    }
}
