package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Client console de l'amateur : se connecte à PORT_AMA.
 * Question : pourrait-il être identique à ClientProg ? Quelle différence reste-t-il (port seulement ?)
 * Si oui, une classe commune évite de dupliquer du code...
 */
public class clientama {

    private static final String HOST = "localhost";
    private static final int PORT_AMA = 4000;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT_AMA);
 
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(),
                            StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(
                    new java.io.OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8), true);
            BufferedReader clavier = new BufferedReader(
                    new InputStreamReader(System.in,
                            StandardCharsets.UTF_8));
 
            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println(ligne.replace("##", System.lineSeparator()));
                if ("NUMERO_SERVICE".equals(ligne)) {
                    out.println(clavier.readLine());
                } else if (ligne.startsWith("Tapez un texte")) {
                    out.println(clavier.readLine());
                }
            }
 
        } catch (IOException e) {
            System.err.println("Problème de connexion : " + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // rien à faire
                }
            }
        }
    }
}