package bri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Le "catalogue" des services disponibles. Classe utilitaire à état partagé :
 * tous les threads (sessions programmeurs ET amateurs) y accèdent en même temps.
 */
public class ServiceRegistry {
    private static final List<ServiceInfo> services =
            Collections.synchronizedList(new ArrayList<ServiceInfo>());

    private ServiceRegistry() {
    }

    /**
     * Vérifie puis installe un service.
     */
    public static void addService(Class<? extends Service> classe,
                                  String loginProgrammeur) {
        String erreurs = ValidateurService.verifier(classe, loginProgrammeur);
        if (erreurs != null) {
            throw new IllegalArgumentException(erreurs);
        }

        synchronized (services) {
            for (ServiceInfo service : services) {
                if (service.getClasse().getName().equals(classe.getName())) {
                    throw new IllegalArgumentException(
                            "Un service portant ce nom est déjà installé");
                }
            }
            services.add(new ServiceInfo(classe, loginProgrammeur));
        }
    }

    /**
     * Remplace le service du même nom appartenant au programmeur.
     */
    public static void updateService(Class<? extends Service> nouvelleClasse,
                                     String loginProgrammeur) {
        String erreurs = ValidateurService.verifier(
                nouvelleClasse, loginProgrammeur);
        if (erreurs != null) {
            throw new IllegalArgumentException(erreurs);
        }

        synchronized (services) {
            for (int i = 0; i < services.size(); i++) {
                ServiceInfo ancien = services.get(i);
                if (ancien.getClasse().getName().equals(nouvelleClasse.getName())) {
                    if (!ancien.getLoginProgrammeur().equals(loginProgrammeur)) {
                        throw new IllegalArgumentException(
                                "Le service appartient à un autre programmeur");
                    }
                    if (ancien.aMemeBytecode(nouvelleClasse)) {
                        throw new IllegalArgumentException(
                                "Le service n'a pas changé");
                    }
                    services.set(i, new ServiceInfo(
                            nouvelleClasse, loginProgrammeur));
                    return;
                }
            }
        }
        throw new IllegalArgumentException(
                "Impossible de mettre à jour un service qui n'est pas installé");
    }

    /**
     * Récupère le service installé à partir d'un indice commençant à zéro.
     */
    public static Class<? extends Service> getServiceClass(int numero) {
        synchronized (services) {
            if (numero < 0 || numero >= services.size()) {
                throw new IndexOutOfBoundsException(
                        "Numéro de service invalide: " + numero);
            }
            return services.get(numero).getClasse();
        }
    }

    /**
     * Récupère uniquement les services actuellement démarrés.
     */
    public static List<ServiceInfo> getServicesDemarres() {
        List<ServiceInfo> result = new ArrayList<ServiceInfo>();
        synchronized (services) {
            for (ServiceInfo service : services) {
                if (service.isDemarre()) {
                    result.add(service);
                }
            }
        }
        return result;
    }

    public static String toStringue() {
        StringBuilder resultat = new StringBuilder("Activités présentes :##");
        synchronized (services) {
            for (int i = 0; i < services.size(); i++) {
                ServiceInfo service = services.get(i);
                resultat.append(i + 1)
                .append(" - ")
                .append(service.nomAffichage())
                .append("##");
            }
        }
        return resultat.toString();
    }
}
