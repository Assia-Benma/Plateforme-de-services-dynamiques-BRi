package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Session d'un programmeur connecté sur PORT_PROG.
 * Une instance = un client = un thread.
 */
public class ServiceProg implements Service {

    private final Socket client;
    private Programmeur programmeur; // null tant que non authentifié

    public ServiceProg(Socket client) {
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
            if (!authentifier(entree, sortie)) {
                sortie.println("FIN");
                return;
            }

            boolean continuer = true;
            while (continuer) {
                sortie.println("MENU##1 - Fournir un service##"
                        + "2 - Mettre à jour un service##"
                        + "3 - Changer l'adresse FTP##0 - Quitter");
                String choix = entree.readLine();
                if (choix == null || "0".equals(choix.trim())) {
                    sortie.println("FIN");
                    continuer = false;
                } else if ("1".equals(choix.trim())) {
                    fournirService(entree, sortie);
                } else if ("2".equals(choix.trim())) {
                    mettreAJourService(entree, sortie);
                } else if ("3".equals(choix.trim())) {
                    changerAdresseFtp(entree, sortie);
                } else {
                    sortie.println("Choix inconnu");
                }
            }
        } catch (IOException e) {
            System.err.println("Session programmeur interrompue : "
                    + e.getMessage());
        }
    }

    private boolean authentifier(BufferedReader entree,
                                 PrintWriter sortie) throws IOException {
        sortie.println("LOGIN");
        String login = entree.readLine();
        sortie.println("MOT_DE_PASSE");
        String motDePasse = entree.readLine();

        programmeur = ProgrammeurRegistry.authentifier(login, motDePasse);
        if (programmeur == null) {
            sortie.println("Authentification refusée");
            return false;
        }

        sortie.println("Authentification réussie");
        return true;
    }

    private void fournirService(BufferedReader entree,
                                PrintWriter sortie) throws IOException {
        sortie.println("NOM_CLASSE");
        String nomClasse = entree.readLine();
        try {
            Class<? extends Service> classe = ChargeurService.charger(
                    programmeur.getUrlFtp(), nomClasse);
            ServiceRegistry.addService(classe, programmeur.getLogin());
            sortie.println("Service installé : " + classe.getName());
        } catch (Exception e) {
            sortie.println("REFUS : " + encoder(e.getMessage()));
        }
    }

    private void mettreAJourService(BufferedReader entree,
                                    PrintWriter sortie) throws IOException {
        sortie.println("NOM_CLASSE");
        String nomClasse = entree.readLine();
        try {
            Class<? extends Service> classe = ChargeurService.charger(
                    programmeur.getUrlFtp(), nomClasse);
            ServiceRegistry.updateService(classe, programmeur.getLogin());
            sortie.println("Service mis à jour : " + classe.getName());
        } catch (Exception e) {
            sortie.println("REFUS : " + encoder(e.getMessage()));
        }
    }

    private void changerAdresseFtp(BufferedReader entree,
                                   PrintWriter sortie) throws IOException {
        sortie.println("NOUVELLE_URL_FTP");
        String url = entree.readLine();
        try {
            programmeur.setUrlFtp(url);
            sortie.println("Adresse FTP modifiée");
        } catch (IllegalArgumentException e) {
            sortie.println("REFUS : " + encoder(e.getMessage()));
        }
    }

    private String encoder(String texte) {
        return texte == null ? "Erreur inconnue" : texte.replace("\r", "")
                .replace("\n", "##");
    }
}
