package bri;

import java.util.HashMap;
import java.util.Map;

public class ProgrammeurRegistry {

    private static final Map<String, Programmeur> programmeurs = new HashMap<>();

    private ProgrammeurRegistry() {
    }

    public static synchronized void ajouter(Programmeur programmeur) {
        if (programmeur == null) {
            throw new IllegalArgumentException("Le programmeur est obligatoire");
        }
        if (programmeurs.containsKey(programmeur.getLogin())) {
            throw new IllegalArgumentException(
                    "Un programmeur utilise deja ce login");
        }
        programmeurs.put(programmeur.getLogin(), programmeur);
    }

    public static synchronized Programmeur authentifier(String login,
                                                        String motDePasse) {
        if (login == null || motDePasse == null) {
            return null;
        }
        Programmeur programmeur = programmeurs.get(login);
        if (programmeur != null && programmeur.verifierMotDePasse(motDePasse)) {
            return programmeur;
        }
        return null;
    }
}
