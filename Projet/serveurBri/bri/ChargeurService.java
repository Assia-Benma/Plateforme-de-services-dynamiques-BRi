package bri;

import java.net.URL;
import java.net.URLClassLoader;

public final class ChargeurService {

    private ChargeurService() {
    }

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

        Class<?> classe = Class.forName(nomClasse.trim(), false, chargeur);
        return classe.asSubclass(Service.class);
    }
}
