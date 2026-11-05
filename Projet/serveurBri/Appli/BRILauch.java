package appli;

import java.net.URLClassLoader;
import java.util.Scanner;

import bri.ServeurBRi;

/**
 * Point d'entrée du serveur. Lance DEUX serveurs en parallèle :
 * un pour les programmeurs (PORT_PROG), un pour les amateurs (PORT_AMA).
 */
public class BRiLaunch {
    private final static int PORT_SERVICE = 3000;

    public static void main(String[] args) {

        // TODO 1 : créer le serveur "programmeurs" avec la bonne classe de session
        // TODO 2 : créer le serveur "amateurs" avec la bonne classe de session
        // TODO 3 : les lancer chacun dans leur propre Thread
        //          Question : pourquoi ne peut-on pas les lancer dans le même thread ?
        // TODO 4 : (option) charger des programmeurs de test dans ProgrammeurRegist


        @SuppressWarnings("resource")
        Scanner clavier = new Scanner(System.in);

        // URLClassLoader sur ftp
        URL classUrl;
        classUrl = new URL("ftp://localhost:2121/classes/");
        URL[] classUrls = { classUrl }
        URLClassLoader urlcl = URLClassLoader.newInstance(classUrls); //ftp:localhost:2121/classes/
        //runnables.Nomclass
        try{

        } catch (ValidationExeception e) {

        }
        //METTRE LES .CLASS DANS "CLASSES" du FTM RES/HOME


        System.out.println("Bienvenue dans votre gestionnaire dynamique d'activit� BRi");
        System.out.println("Pour ajouter une activit�, celle-ci doit �tre pr�sente sur votre serveur ftp");
        System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'int�grer");
        System.out.println("Les clients se connectent au serveur 3000 pour lancer une activit�");

        new Thread(new ServeurBRi(PORT_SERVICE)).start();

        while (true){
            try {
                String classeName = clavier.next();
                ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
                //Petit affichage "class bien chargé"
                // charger la classe et la d�clarer au ServiceRegistry
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
