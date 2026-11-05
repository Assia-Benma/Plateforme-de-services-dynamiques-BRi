import java.net.Socket;

/**
 * Session d'un programmeur connecté sur PORT_PROG.
 * Une instance = un client = un thread.
 */
public class ServiceProg implements Service {

    private final Socket client;
    private Programmeur programmeur; // null tant que non authentifié

    public ServiceProg(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        // TODO 1 : ouvrir les flux (BufferedReader / PrintWriter) sur le socket
        // TODO 2 : authentification (demander login puis mot de passe) ; que faire en cas d'échec ?
        // TODO 3 : boucle de menu : envoyer le menu, lire le choix, appeler la bonne méthode
        // TODO 4 : fermer proprement le socket (try-with-resources ? finally ?)
    }

    // --- Les actions du menu ---

    // TODO : demander le nom de la classe, la charger depuis le ftp, valider, enregistrer.
    //        Quelles erreurs peuvent arriver ? Que répondre au client à chaque fois ?
    private void fournirService() { }

    // TODO : comme fournir, mais la classe existe déjà et appartient au programmeur
    //        Question : un programmeur peut-il modifier le service d'un AUTRE ? (non !)
    private void mettreAJourService() { }

    // TODO : changer l'url ftp du programmeur
    private void changerAdresseFtp() { }

    // TODO (option) : démarrer / arrêter / désinstaller un de SES services
    private void demarrerArreterService() { }
    private void desinstallerService() { }

    // Aide : petite méthode pour envoyer du texte multi-ligne sur une ligne (remplacer "\n" par "##")
    // TODO : pourquoi est-ce nécessaire avec readLine() côté client ?
    private String encoder(String texte) { return null; }
}
