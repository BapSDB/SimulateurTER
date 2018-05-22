
package traducteur.sw2;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import java.io.BufferedWriter;
import traducteur.FabriqueTraducteur;
import traducteur.Traducteur;
import util.TimeStamp;
import util.Util;
import static traducteur.sw2.TraducteurSwitch2.SEPARATEUR;

public class FabriqueTraducteurSwitch2 extends FabriqueTraducteur {
    
    public FabriqueTraducteurSwitch2(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig, String nomFichierCSV) {
	super(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV);
        
	traduireLigne = (String ligne, String donnees, int numLigne, BufferedWriter config) -> {
	    if (donnees != null) {
		String [] evenements = donnees.split(SEPARATEUR) ;
		TimeStamp.verifierDate(evenements, nomFichierOriginal, numLigne);
                tableauCSV.lireValeur(evenements[0], "power", evenements[3]);
		return evenements[0] + Util.SEPARATEUR + evenements[2].substring(0, evenements[2].length()-1) + Util.SEPARATEUR + evenements[3] ;
	    }
	    return null ;
	} ;
	
    }

    @Override
    public Traducteur creer() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	return new TraducteurSwitch2(this) ;
    }
    
}
