package configurateur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigNomObjetException;
import exceptions.config.ConfigTraiterFichierExceptions;
import java.util.Set;
import java.util.regex.Pattern;
import simulateur.FabriqueSimulateur;
import util.Util;

public final class Configurateur {
    
    private final FabriqueConfigurateur fc ;

    public Configurateur(FabriqueConfigurateur fc) throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	this.fc = fc ;
	lireObjets() ;
    }
    
    /**
     * @return l'ensemble des noms d'objets
     */
    public Set<String> getNomsObjets() {
	return fc.traducteur.getNomsObjets().keySet() ;
    }
    
    /**
     * 
     * @param nomObjet
     * Le nom d'objet pour lequel on souhaite retrouver l'indice de la valeur associée dans le {@link simulateur.Simulateur#tableau}
     * @return l'indice de la valeur associée au nom d'objet passé en paramètre
     */
    public int getNomObjetVersIndice(String nomObjet) {
	return fc.traducteur.getNomsObjets().get(nomObjet) ;
    }

    public String getNomFichierOEBL() {
	return fc.traducteur.getNomFichierOEBL() ;
    }
    
    /**
     * Lit un fichier de configuration listant les objets utilisés dans la simulation
     * @param nomFichierEntree
     * Le chemin (absolu ou relatif) <p> + nom du fichier de configuration à lire.
     * @throws ConfigNomObjetException
     * si le un nom d'objet ne respecte pas la convention de nommage.
     * @throws ConfigFichierIntrouvableException
     * si le fichier de configuration n'existe pas.
     * @throws EntreeSortieException
     * si une erreur d'entrée/sortie est apparue
     */
    
    public void lireObjets (String nomFichierEntree) throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	Util.lireDonnees(Pattern.compile("\\w+"), fc.ajouterElement, new ConfigTraiterFichierExceptions(nomFichierEntree));
    }
    
    public void lireObjets () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	
    }
    
    public FabriqueSimulateur nouveauSimulateur () {
	return new FabriqueSimulateur(this) ;
    }
    
}
