package bri;


import java.io.*;
import java.net.*;

/**
 * Serveur TCP générique : écoute sur un port et, pour chaque client qui se connecte,
 * crée une session (une instance de la classe de service donnée) dans un nouveau Thread.
 *
 * Le même code sert pour le port programmeur ET le port amateur :
 * seule la classe de session change (ServiceProg ou ServiceAma).
 */
public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private final Class<? extends Service> classeSession; // TODO : pourquoi une Class et pas une instance ?


	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(int port, Class<? extends Service> classeSession) {
 		this.listenSocket = new ServerSocket(port);
        this.classeSession = classeSession;
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	@Override
	public void run() {
		// TODO : boucle infinie :
        //   1. accept()
        //   2. instancier classeSession avec le Socket reçu (réflexion : getConstructor(Socket.class).newInstance(...))
        //   3. lancer l'instance dans un Thread
        // Question : que se passe-t-il si une session plante ? Le serveur doit-il s'arrêter ?
		try {
            while (!listenSocket.isClosed()) {
                Socket client = listenSocket.accept();
                try {
                    Service session = classeSession
                            .getConstructor(Socket.class)
                            .newInstance(client);
                    new Thread(session).start();
                } catch (ReflectiveOperationException e) {
                    // problème de session : on refuse CE client, le serveur continue
                    System.err.println("Impossible de créer la session : " + e);
                    try { client.close(); } catch (IOException ignored) {}
                }
            }
        } catch (IOException e) {
            if (!listenSocket.isClosed()) {
                System.err.println("Problème sur le port d'écoute : " + e);
            }
        } finally {
            fermer();
        }

 	// TODO : une méthode pour fermer proprement le ServerSocket (finalize est déconseillé, pensez à autre chose)
	 // restituer les ressources --> finalize
    public void lancer() {
        new Thread(this).start();
    }

    public void fermer() {
        try {
            listenSocket.close();
        } catch (IOException e) {
            // rien à faire : on ferme de toute façon
        }
    }
}
