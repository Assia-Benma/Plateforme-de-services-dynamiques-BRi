package bri;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Représente un service installé dans la plateforme :
 * la classe + son propriétaire + son état (démarré / arrêté).
 * (Utile surtout pour les options démarrer/arrêter et désinstaller.)
 */
public class ServiceInfo {

    private final Class<? extends Service> classe;
    private final String loginProgrammeur;
    private final byte[] empreinteBytecode;
    private boolean demarre;

    public ServiceInfo(Class<? extends Service> classe, String loginProgrammeur) {
        if (classe == null) {
            throw new IllegalArgumentException("La classe du service est obligatoire");
        }
        if (loginProgrammeur == null || loginProgrammeur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le login du programmeur est obligatoire");
        }
        this.classe = classe;
        this.loginProgrammeur = loginProgrammeur.trim();
        this.empreinteBytecode = calculerEmpreinteBytecode(classe);
        this.demarre = true;
    }

    public Class<? extends Service> getClasse() {
        return classe;
    }

    public String getLoginProgrammeur() {
        return loginProgrammeur;
    }

    public boolean aMemeBytecode(Class<? extends Service> autreClasse) {
        return autreClasse != null
                && Arrays.equals(empreinteBytecode,
                calculerEmpreinteBytecode(autreClasse));
    }

    public synchronized boolean isDemarre() {
        return demarre;
    }

    public synchronized void setDemarre(boolean demarre) {
        this.demarre = demarre;
    }

    /**
     * Appelle la méthode statique public toStringue() du service.
     */
    public String nomAffichage() {
        try {
            Method methode = classe.getDeclaredMethod("toStringue");
            Object resultat = methode.invoke(null);
            return (String) resultat;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "Le service ne possède pas toStringue(): " + classe.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    "toStringue() n'est pas accessible: " + classe.getName(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(
                    "toStringue() a échoué: " + classe.getName(), e.getCause());
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "toStringue() ne renvoie pas une chaîne: " + classe.getName(), e);
        }
    }

    private static byte[] calculerEmpreinteBytecode(
            Class<? extends Service> classe) {
        String chemin = "/" + classe.getName().replace('.', '/') + ".class";
        try (InputStream entree = classe.getResourceAsStream(chemin)) {
            if (entree == null) {
                throw new IllegalStateException(
                        "Impossible de lire le bytecode de " + classe.getName());
            }

            MessageDigest condensateur = MessageDigest.getInstance("SHA-256");
            byte[] tampon = new byte[4096];
            int lus;
            while ((lus = entree.read(tampon)) != -1) {
                condensateur.update(tampon, 0, lus);
            }
            return condensateur.digest();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossible de lire le bytecode de " + classe.getName(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 est indisponible", e);
        }
    }
}