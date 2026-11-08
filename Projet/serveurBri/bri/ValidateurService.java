package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;

/**
 * Vérifie par réflexion qu'une classe respecte la norme BRi.
 * Elle sera appelée quand un programmeur fournit ou met à jour un service.
 */
public final class ValidateurService {

    private ValidateurService() {
    }

    /**
     * @return null si la classe est valide, sinon un message expliquant ce qui ne va pas
     *         (pour le renvoyer au programmeur : c'est bien plus utile qu'un simple boolean).
     */
    public static String verifier(Class<?> classe, String loginProgrammeur) {
        StringBuilder erreurs = new StringBuilder();

        if (classe == null) {
            return "La classe à vérifier est obligatoire";
        }

        if (loginProgrammeur == null || loginProgrammeur.trim().isEmpty()) {
            erreurs.append("Le login du programmeur est obligatoire\n");
        }

        int modificateursClasse = classe.getModifiers();
        if (!Service.class.isAssignableFrom(classe)) {
            erreurs.append("La classe n'implémente pas bri.Service\n");
        }
        if (Modifier.isAbstract(modificateursClasse)) {
            erreurs.append("La classe est abstraite\n");
        }
        if (!Modifier.isPublic(modificateursClasse)) {
            erreurs.append("La classe n'est pas publique\n");
        }

        if (loginProgrammeur != null && !loginProgrammeur.trim().isEmpty()) {
            Package paquet = classe.getPackage();
            String nomPackage = paquet == null ? "" : paquet.getName();
            if (!nomPackage.equals(loginProgrammeur)
                    && !nomPackage.startsWith(loginProgrammeur + ".")) {
                erreurs.append("La classe doit être dans le package ")
                        .append(loginProgrammeur)
                        .append('\n');
            }
        }

        verifierConstructeur(classe, erreurs);
        verifierAttributSocket(classe, erreurs);
        verifierToStringue(classe, erreurs);

        return erreurs.length() == 0 ? null : erreurs.toString().trim();
    }

    private static void verifierConstructeur(Class<?> classe,
                                             StringBuilder erreurs) {
        try {
            Constructor<?> constructeur = classe.getConstructor(Socket.class);
            if (constructeur.getExceptionTypes().length > 0) {
                erreurs.append("Le constructeur public (Socket) lève une exception\n");
            }
        } catch (NoSuchMethodException e) {
            erreurs.append("Il manque un constructeur public (Socket)\n");
        }
    }

    private static void verifierAttributSocket(Class<?> classe,
                                               StringBuilder erreurs) {
        boolean attributCorrect = false;

        for (Field champ : classe.getDeclaredFields()) {
            int modificateurs = champ.getModifiers();
            if (champ.getType() == Socket.class
                    && Modifier.isPrivate(modificateurs)
                    && Modifier.isFinal(modificateurs)) {
                attributCorrect = true;
                break;
            }
        }

        if (!attributCorrect) {
            erreurs.append("Il manque l'attribut private final Socket\n");
        }
    }

    private static void verifierToStringue(Class<?> classe,
                                           StringBuilder erreurs) {
        try {
            Method methode = classe.getDeclaredMethod("toStringue");
            int modificateurs = methode.getModifiers();

            if (!Modifier.isPublic(modificateurs)
                    || !Modifier.isStatic(modificateurs)) {
                erreurs.append("toStringue doit être public static\n");
            }
            if (methode.getReturnType() != String.class) {
                erreurs.append("toStringue doit renvoyer String\n");
            }
            if (methode.getExceptionTypes().length > 0) {
                erreurs.append("toStringue ne doit lever aucune exception\n");
            }
        } catch (NoSuchMethodException e) {
            erreurs.append("Il manque la méthode public static String toStringue()\n");
        }
    }
}
