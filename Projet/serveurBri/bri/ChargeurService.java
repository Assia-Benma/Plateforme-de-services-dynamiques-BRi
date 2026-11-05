/**
 * Charge dynamiquement une classe depuis le serveur FTP du programmeur.
 */
public class ChargeurService {

    /**
     * @param urlFtp   adresse du serveur ftp du programmeur
     * @param nomClasse nom complet de la classe (ex: "brette.ServiceInversion")
     */
    public static Class<? extends Service> charger(String urlFtp, String nomClasse) throws Exception {
        // TODO : utiliser un URLClassLoader construit à partir de l'URL ftp
        // Questions :
        //  - Que se passe-t-il si on recharge une classe avec le MÊME ClassLoader (mise à jour) ?
        //    -> pourquoi faut-il un nouveau ClassLoader à chaque mise à jour ?
        //  - Comment s'assurer que la classe chargée est bien un Service (cast / asSubclass) ?
        return null;
    }
}
