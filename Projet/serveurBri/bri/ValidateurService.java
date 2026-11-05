
/**
 * Vérifie par réflexion qu'une classe respecte la norme BRi.
 * Elle sera appelée quand un programmeur fournit ou met à jour un service.
 */
public class ValidateurService {

    /**
     * @return null si la classe est valide, sinon un message expliquant ce qui ne va pas
     *         (pour le renvoyer au programmeur : c'est bien plus utile qu'un simple boolean).
     */
    public static String verifier(Class<?> classe, String loginProgrammeur) {
        // TODO : vérifier, une à une, les règles de la norme :
        //   - implémente Service ?
        //   - classe publique et non abstraite ? (Modifier.isPublic / isAbstract)
        //   - bon package (= login du programmeur) ? (classe.getPackage().getName())
        //   - constructeur public avec un Socket ?
        //   - attribut Socket private final ?
        //   - méthode statique publique toStringue() qui renvoie un String ?
        //   - (option) pas de classe déjà existante avec ce nom (unicité)
        return null;
    }
}
