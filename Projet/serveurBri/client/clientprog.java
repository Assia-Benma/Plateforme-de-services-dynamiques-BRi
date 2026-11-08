package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Client console du programmeur : se connecte à PORT_PROG.
 * Le client est "bête" : il affiche ce que dit le serveur et envoie ce que tape l'utilisateur.
 */
public class clientprog {
    private static final String HOST = "localhost";
    private static final int PORT_PROG = 3000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT_PROG);
             BufferedReader serveur = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(),
                             StandardCharsets.UTF_8));
             PrintWriter sortie = new PrintWriter(
                     new java.io.OutputStreamWriter(
                             socket.getOutputStream(),
                             StandardCharsets.UTF_8), true);
             BufferedReader clavier = new BufferedReader(
                     new InputStreamReader(System.in,
                             StandardCharsets.UTF_8))) {
            String message;
            while ((message = serveur.readLine()) != null) {
                System.out.println(message.replace("##", System.lineSeparator()));
                if ("FIN".equals(message)) {
                    break;
                }
                if (attendReponse(message)) {
                    String reponse = clavier.readLine();
                    if (reponse == null) {
                        break;
                    }
                    sortie.println(reponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Problème de connexion : " + e.getMessage());
        }
    }

    private static boolean attendReponse(String message) {
        return "LOGIN".equals(message)
                || "MOT_DE_PASSE".equals(message)
                || "NOM_CLASSE".equals(message)
                || "NOUVELLE_URL_FTP".equals(message)
                || message.startsWith("MENU##");
    }
}
