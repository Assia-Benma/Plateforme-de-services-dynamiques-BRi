package serveurBri.bri;

/**
 * La "norme BRi" : toute classe de service doit respecter ce contrat.
 * (Reprenez exactement la norme que vous avez testée au tp5 !)
 *
 * Rappel de ce qu'on exige en général d'une classe de service :
 *  - implémenter cette interface
 *  - être publique, non abstraite
 *  - avoir un constructeur public prenant un Socket
 *  - avoir un attribut Socket private final
 *  - avoir une méthode statique public String toStringue()
 *  - ne pas lever d'exception non gérée
 */
public interface Service extends Runnable {
    // TODO : que doit-on ajouter ici ? Réfléchissez : Runnable donne déjà run().
    // Une méthode statique peut-elle être déclarée dans une interface pour être
    // imposée aux classes ? Non... comment la norme peut-elle alors la vérifier ?
    // (indice : ValidateurService + réflexion)
}
