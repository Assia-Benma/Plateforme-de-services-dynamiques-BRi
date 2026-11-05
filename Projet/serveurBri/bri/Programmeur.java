/** Un programmeur inscrit sur la plateforme (compte supposé créé automatiquement). */
public class Programmeur {

    private final String login;
    private final String motDePasse;
    private String urlFtp;

    public Programmeur(String login, String motDePasse, String urlFtp) {
        // TODO
        this.login = login;
        this.motDePasse = motDePasse;
        this.urlFtp = urlFtp;
    }

    // TODO : getLogin(), getUrlFtp(), setUrlFtp(String)
    // TODO : verifierMotDePasse(String) -- pourquoi vaut-il mieux ne pas exposer getMotDePasse() ?
}
