/**
 * Client console du programmeur : se connecte à PORT_PROG.
 * Le client est "bête" : il affiche ce que dit le serveur et envoie ce que tape l'utilisateur.
 */
public class clientprog {
    private static final String HOST = "localhost";
    private static final int PORT_PROG = 3000;

    public static void main(String[] args) {
        // TODO 1 : ouvrir le socket et les flux
        // TODO 2 : boucle : lire une ligne du serveur -> l'afficher (décoder "##" en "\n"),
        //          puis lire le clavier -> l'envoyer
        // Question : comment le client sait-il que c'est à lui de parler ? (protocole : qui parle quand ?)
        // TODO 3 : condition d'arrêt + fermeture propre
    }
