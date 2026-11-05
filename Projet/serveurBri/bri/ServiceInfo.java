/**
 * Représente un service installé dans la plateforme :
 * la classe + son propriétaire + son état (démarré / arrêté).
 * (Utile surtout pour les options démarrer/arrêter et désinstaller.)
 */
public class ServiceInfo {

    private final Class<? extends Service> classe;
    private final String loginProgrammeur;
    private boolean demarre;

    public ServiceInfo(Class<? extends Service> classe, String loginProgrammeur) {
        // TODO
        this.classe = classe;
        this.loginProgrammeur = loginProgrammeur;
    }

    // TODO : getters, isDemarre(), setDemarre(boolean)
    // Question : ces accès peuvent venir de plusieurs threads. Que risque-t-on ? (mot-clé : volatile / synchronized)

    // TODO : une méthode qui renvoie le nom du service à afficher à l'amateur
    //        (aide : toStringue() de la classe, via réflexion)
}