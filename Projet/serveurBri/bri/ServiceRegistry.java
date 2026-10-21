package bri;

import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partag�e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = null;
	}
	private static List<Class<?>> servicesClasses;

// ajoute une classe de service apr�s contr�le de la norme BLTi
	public static void addService(Class<? extends Service> runclass) {
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
	
// renvoie la classe de service (numService -1)	
	public static Class<? extends Service> getServiceClass(int numService) {
		
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
