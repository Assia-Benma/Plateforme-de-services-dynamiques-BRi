package bri;

import java.util.List;


/**
 * Le "catalogue" des services disponibles. Classe utilitaire à état partagé :
 * tous les threads (sessions programmeurs ET amateurs) y accèdent en même temps.
 */
public class ServiceRegistry {
	// cette classe est un registre de services
	// partag�e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = null;
	}
	private static List<Class<?>> servicesClasses;

	private static final List<ServiceInfo> services = new ArrayList<>();
    // TODO : cette liste est partagée entre threads. Comment la protéger ?
    //        (synchronized sur les méthodes ? Collections.synchronizedList ? Que choisir et pourquoi ?)

    // TODO : ajouter un service (que faire s'il existe déjà ? rappelez-vous l'unicité de la classe)
	public static void addService(Class<? extends Service> classe, String loginProgrammeur) {
		try{
			//
			//validation

		} catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }

		// qui extends Services
		// v�rifier la conformit� par introspection
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector
	}
	
    // TODO : mettre à jour un service (remplacer l'ancienne classe par la nouvelle)
    //        Attention : les amateurs qui utilisent l'ancienne version ne doivent pas planter.
    public static void updateService(Class<? extends Service> nouvelleClasse, String loginProgrammeur) 
	{ 

	}

    // TODO : récupérer la classe du service numéro n (les numéros commencent à 1 pour l'amateur, à 0 pour la liste !)
    public static Class<? extends Service> getServiceClass(int numero) 
	{ 
		return null; 
	}

// Validation qui verifie un bon toStringue (Comme TP 2)
	public static void validation () {
		//***********************************************
	}
	
// liste les activit�s pr�sentes
	public static String toStringue() {
		String result = "Activit�s pr�sentes :##";
		// todo
		//faire une boucle
		// faire du invoke avec du toString
		return result;
	}

}
