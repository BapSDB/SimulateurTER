package configurateur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.fichier_config.ConfigFichierIntrouvableException;
import exceptions.fichier_config.ConfigNomObjetException;
import exceptions.fichier_config.ConfigNomObjetNonUniqueException;
import exceptions.fichier_config.ConfigTraiterFichierExceptions;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import util.Util;
import util.Util.AjouterElement;

public final class Configurateur {
    
    private Set<String> nomsObjets = new LinkedHashSet<>();
    private final AjouterElement ajouterElement ;

    public Configurateur(String nomFichierEntree) throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	
	ajouterElement = (String ligne, String donnees, int numLigne) -> {
	    if (donnees != null) {
                if (!nomsObjets.contains(donnees)) {
                    nomsObjets.add(donnees);
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
	return nomsObjets ;
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
    
    /*public final void lireObjets (String nomFichierEntree) throws ConfigNomObjetException, ConfigFichierIntrouvableException {
	try (Scanner scanner = new Scanner(new File(nomFichierEntree))) {
	    scanner.useDelimiter("");
	    int numLigne = 0 ;
	    while (scanner.hasNext()) {
		while (scanner.hasNext(SKIP)) {
		    scanner.skip(SKIP);
		    numLigne++ ;
		}
		if (scanner.hasNext()) {
		    String ligne = scanner.nextLine() ;
		    try(Scanner scannerLigne = new Scanner(ligne)) {
			String nomObjet = scannerLigne.findInLine("\\w+") ;
			numLigne++ ;
			if (nomObjet != null) {
			    nomsObjets.add(nomObjet);
			} else {
			    throw new ConfigNomObjetException(nomObjet, numLigne, nomFichierEntree) ;
			}
		    }
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw new ConfigFichierIntrouvableException(ex, nomFichierEntree);
	}
    }*/
    
}
