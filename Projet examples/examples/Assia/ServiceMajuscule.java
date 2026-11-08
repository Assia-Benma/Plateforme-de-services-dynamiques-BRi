package Assia;

import bri.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ServiceMajuscule implements Service {

    private final Socket client;

    public ServiceMajuscule(Socket client) {
        if (client == null) {
            throw new IllegalArgumentException("La socket est obligatoire");
        }
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader entree = new BufferedReader(
                     new InputStreamReader(client.getInputStream(),
                             StandardCharsets.UTF_8));
             PrintWriter sortie = new PrintWriter(
                     new java.io.OutputStreamWriter(
                             client.getOutputStream(),
                             StandardCharsets.UTF_8), true)) {
            sortie.println("Tapez un texte a convertir en majuscules");
            String texte = entree.readLine();
            if (texte != null) {
                sortie.println(texte.toUpperCase(Locale.ROOT));
            }
        } catch (IOException e) {
            System.err.println("Fin du service majuscule : "
                    + e.getMessage());
        } finally {
            fermerSocket();
        }
    }

    private void fermerSocket() {
        try {
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            System.err.println("Impossible de fermer la socket : "
                    + e.getMessage());
        }
    }

    public static String toStringue() {
        return "Conversion en majuscules";
    }
}
