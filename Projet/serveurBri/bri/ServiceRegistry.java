package bri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceRegistry {
    private static final List<ServiceInfo> services =
            Collections.synchronizedList(new ArrayList<ServiceInfo>());

    private ServiceRegistry() {
    }

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
                            "Un service portant ce nom est deja installe");
                }
            }
            services.add(new ServiceInfo(classe, loginProgrammeur));
        }
    }

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
                                "Le service appartient a un autre programmeur");
                    }
                    if (ancien.aMemeBytecode(nouvelleClasse)) {
                        throw new IllegalArgumentException(
                                "Le service n'a pas change");
                    }
                    services.set(i, new ServiceInfo(
                            nouvelleClasse, loginProgrammeur));
                    return;
                }
            }
        }
        throw new IllegalArgumentException(
                "Impossible de mettre a jour un service qui n'est pas installe");
    }

    public static Class<? extends Service> getServiceClass(int numero) {
        synchronized (services) {
            if (numero < 0 || numero >= services.size()) {
                throw new IndexOutOfBoundsException(
                        "Numero de service invalide: " + numero);
            }
            return services.get(numero).getClasse();
        }
    }

    public static String toStringue() {
        StringBuilder resultat = new StringBuilder("Activites presentes :##");
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
