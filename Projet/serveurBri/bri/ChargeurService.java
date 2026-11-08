package bri;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Charge dynamiquement une classe depuis le serveur FTP du programmeur.
 */
public final class ChargeurService {

    private ChargeurService() {
    }

    /**
     * @param urlFtp   adresse du serveur ftp du programmeur
     * @param nomClasse nom complet de la classe (ex: "Alicia.ServiceInversion")
     */
    public static Class<? extends Service> charger(String urlFtp,
                                                   String nomClasse)
            throws Exception {
        if (urlFtp == null || urlFtp.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL FTP est obligatoire");
        }
        if (nomClasse == null || nomClasse.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de classe est obligatoire");
        }

        String adresse = urlFtp.trim();
        if (!adresse.endsWith("/")) {
            adresse += "/";
        }

        URL dossierClasses = new URL(adresse);
        URLClassLoader chargeur = new URLClassLoader(
                new URL[]{dossierClasses},
                ChargeurService.class.getClassLoader());

        /*
         * false signifie : charger la classe sans exécuter son bloc static.
         * La validation doit avoir lieu avant l'initialisation du service.
         *
         * Un nouveau chargeur est créé à chaque appel : c'est nécessaire pour
         * qu'une nouvelle version d'une classe puisse être chargée après une
         * mise à jour sur le FTP.
         */
        Class<?> classe = Class.forName(nomClasse.trim(), false, chargeur);
        return classe.asSubclass(Service.class);
    }
}
