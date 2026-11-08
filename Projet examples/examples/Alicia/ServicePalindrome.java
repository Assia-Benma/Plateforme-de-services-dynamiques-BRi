package Alicia;

import bri.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServicePalindrome implements Service {

    private final Socket client;

    public ServicePalindrome(Socket client) {
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
            sortie.println("Tapez un texte a tester");
            String texte = entree.readLine();
            if (texte != null) {
                String normalise = texte.replaceAll("\\s+", "")
                        .toLowerCase();
                boolean palindrome = normalise.equals(
                        new StringBuilder(normalise).reverse().toString());
                sortie.println(palindrome
                        ? "C'est un palindrome"
                        : "Ce n'est pas un palindrome");
            }
        } catch (IOException e) {
            System.err.println("Fin du service palindrome : "
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
        return "Test de palindrome";
    }
}
