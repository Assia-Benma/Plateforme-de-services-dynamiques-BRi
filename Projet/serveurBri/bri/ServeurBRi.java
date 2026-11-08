package bri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public final class ServeurBRi implements Runnable {

    private final ServerSocket listenSocket;
    private final Class<? extends Service> classeSession;

    public ServeurBRi(int port, Class<? extends Service> classeSession)
            throws IOException {
        if (classeSession == null) {
            throw new IllegalArgumentException("La classe de session est obligatoire");
        }
        this.listenSocket = new ServerSocket(port);
        this.classeSession = classeSession;
    }

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
                System.err.println("Le constructeur de la session a echoue : "
                        + invocation.getCause());
            } else {
                System.err.println("Impossible de creer la session : " + e);
            }
        }
    }

    public void lancer() {
        Thread thread = new Thread(this, "bri-serveur-" + listenSocket.getLocalPort());
        thread.start();
    }

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
