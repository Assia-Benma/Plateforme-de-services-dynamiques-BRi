
import java.util.HashMap;
import java.util.Map;

/** Annuaire des programmeurs (login -> Programmeur). Partagé entre threads. */
public class ProgrammeurRegistry {

    private static final Map<String, Programmeur> programmeurs = new HashMap<>();
    // TODO : accès concurrents -> quelle structure ou quelle protection ?

    // TODO : ajouter un programmeur (inscription supposée automatique)
    public static void ajouter(Programmeur p) { }

    // TODO : authentifier : renvoie le Programmeur si login/mdp corrects, null sinon
    public static Programmeur authentifier(String login, String mdp) { return null; }
}
