package configurateur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.fichier_config.ConfigFichierIntrouvableException;
import exceptions.fichier_config.ConfigNomObjetException;
import exceptions.fichier_config.ConfigNomObjetNonUniqueException;
import exceptions.fichier_config.ConfigTraiterFichierExceptions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import util.Util;
import util.Util.AjouterElement;

public final class Configurateur {
    
    private Map<String,Integer> nomsObjets = new LinkedHashMap<>();
    private final AjouterElement ajouterElement ;

    public Configurateur(String nomFichierEntree) throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	
	ajouterElement = (String ligne, String donnees, int numLigne) -> {
	    if (donnees != null) {
                if (!nomsObjets.containsKey(donnees)) {
                    nomsObjets.put(donnees, nomsObjets.size());
                } else {
                    throw new ConfigNomObjetNonUniqueException(ligne, numLigne, nomFichierEntree) ;
                }
	    } else {
		throw new ConfigNomObjetException(ligne, numLigne, nomFichierEntree) ;
	    }
	};
	
	lireObjets(nomFichierEntree);
    }
    
    /**
     * @return l'ensemble des noms d'objets
     */
    public Set<String> getNomsObjets() {
	return nomsObjets.keySet() ;
    }
    
    /**
     * 
     * @param nomObjet
     * Le nom d'objet pour lequel on souhaite retrouver l'indice de la valeur associée dans le {@link simulateur.Simulateur#tableau}
     * @return l'indice de la valeur associée au nom d'objet passé en paramètre
     */
    public int getNomObjetVersIndice(String nomObjet) {
	return nomsObjets.get(nomObjet) ;
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
	Util.lireDonnees(nomFichierEntree, Pattern.compile("\\w+"), ajouterElement, new ConfigTraiterFichierExceptions(nomFichierEntree));
    }
    
}
