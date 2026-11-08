package Appli;

import bri.ServiceAma;
import bri.ServiceProg;
import bri.ServeurBRi;
import bri.Programmeur;
import bri.ProgrammeurRegistry;

public final class BRiLaunch {

    private static final int PORT_PROG = 3000;
    private static final int PORT_AMA = 4000;

    private BRiLaunch() {
    }

    public static void main(String[] args) throws Exception {
        ProgrammeurRegistry.ajouter(new Programmeur(
                "Alicia", "nightwing", "ftp://localhost:2121/classes/"));
        ProgrammeurRegistry.ajouter(new Programmeur(
                "Assia", "assiette", "ftp://localhost:2121/classes/"));
        ProgrammeurRegistry.ajouter(new Programmeur(
                "Ines", "armin", "ftp://localhost:2121/classes/"));

        ServeurBRi serveurProgrammeurs =
                new ServeurBRi(PORT_PROG, ServiceProg.class);
        ServeurBRi serveurAmateurs =
                new ServeurBRi(PORT_AMA, ServiceAma.class);

        serveurProgrammeurs.lancer();
        serveurAmateurs.lancer();

        System.out.println("BRiLaunch demarre.");
        System.out.println("Port programmeurs : " + PORT_PROG);
        System.out.println("Port amateurs : " + PORT_AMA);
    }
}
