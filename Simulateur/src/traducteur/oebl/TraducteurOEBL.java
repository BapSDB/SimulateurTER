
package traducteur.oebl;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import traducteur.Traducteur;

public class TraducteurOEBL extends Traducteur {

    public TraducteurOEBL(FabriqueTraducteurOEBL ft) {
	super(ft);
    }

    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	
    }
    
}
