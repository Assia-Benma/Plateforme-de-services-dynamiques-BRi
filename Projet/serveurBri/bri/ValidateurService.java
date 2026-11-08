package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;

public final class ValidateurService {

    private ValidateurService() {
    }

    public static String verifier(Class<?> classe, String loginProgrammeur) {
        StringBuilder erreurs = new StringBuilder();

        if (classe == null) {
            return "La classe a verifier est obligatoire";
        }

        if (loginProgrammeur == null || loginProgrammeur.trim().isEmpty()) {
            erreurs.append("Le login du programmeur est obligatoire\n");
        }

        int modificateursClasse = classe.getModifiers();
        if (!Service.class.isAssignableFrom(classe)) {
            erreurs.append("La classe n'implemente pas bri.Service\n");
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
                erreurs.append("La classe doit etre dans le package ")
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
                erreurs.append("Le constructeur public (Socket) leve une exception\n");
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
                erreurs.append("toStringue doit etre public static\n");
            }
            if (methode.getReturnType() != String.class) {
                erreurs.append("toStringue doit renvoyer String\n");
            }
            if (methode.getExceptionTypes().length > 0) {
                erreurs.append("toStringue ne doit lever aucune exception\n");
            }
        } catch (NoSuchMethodException e) {
            erreurs.append("Il manque la methode public static String toStringue()\n");
        }
    }
}
