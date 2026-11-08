package bri;

/** Un programmeur inscrit sur la plateforme (compte supposé créé automatiquement). */
public class Programmeur {

    private final String login;
    private final String motDePasse;
    private String urlFtp;

    public Programmeur(String login, String motDePasse, String urlFtp) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Le login est obligatoire");
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        if (urlFtp == null || urlFtp.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL FTP est obligatoire");
        }
        this.login = login;
        this.motDePasse = motDePasse;
        this.urlFtp = urlFtp.trim();
    }

    public String getLogin() {
        return login;
    }

    public synchronized String getUrlFtp() {
        return urlFtp;
    }

    public synchronized void setUrlFtp(String urlFtp) {
        if (urlFtp == null || urlFtp.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL FTP est obligatoire");
        }
        this.urlFtp = urlFtp.trim();
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }
}
