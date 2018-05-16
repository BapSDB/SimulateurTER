
package configurateur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.config.ConfigNomObjetException;
import exceptions.config.ConfigNomObjetNonUniqueException;
import traducteur.Traducteur;
import util.Util.AjouterElement;

public class FabriqueConfigurateur {
    
    Traducteur traducteur ;
    
    final AjouterElement ajouterElement = (String ligne, String donnees, int numLigne) -> {
	if (donnees != null) {
	    if (!traducteur.getNomsObjets().containsKey(donnees)) {
		traducteur.getNomsObjets().put(donnees, traducteur.getNomsObjets().size());
	    } else {
		throw new ConfigNomObjetNonUniqueException(ligne, numLigne, traducteur.getNomFichierConfig()) ;
	    }
	} else {
	    throw new ConfigNomObjetException(ligne, numLigne, traducteur.getNomFichierConfig()) ;
	}
    };
    
    public FabriqueConfigurateur(Traducteur traducteur) {
	this.traducteur = traducteur ;
    }
    
    public Configurateur creer () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	return new Configurateur(this) ;
    }
    
}
