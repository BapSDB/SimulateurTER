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

    public Set<String> getNomsObjets() {
	return nomsObjets.keySet() ;
    }
    
    public int getNomObjetVersIndice(String nomObjet) {
	return nomsObjets.get(nomObjet) ;
    }
    
    /**
     * Lit un fichier de configuration listant les objets utilisés dans la simulation
     * @param nomFichierEntree
     * Le chemin (absolu ou relatif) <p> + nom du fichier de configuration à lire.
     * @throws ConfigNomObjetException
     * @throws ConfigFichierIntrouvableException
     */
    
    public void lireObjets (String nomFichierEntree) throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	Util.lireDonnees(nomFichierEntree, Pattern.compile("\\w+"), ajouterElement, new ConfigTraiterFichierExceptions(nomFichierEntree));
    }
    
}
