
package traducteur.oebl;

import traducteur.FabriqueTraducteur;
import traducteur.Traducteur;

public class FabriqueTraducteurOEBL extends FabriqueTraducteur {

    public FabriqueTraducteurOEBL(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig, String nomFichierCSV) {
	super(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV);
    }

    @Override
    public Traducteur creer() {
	return new TraducteurOEBL(this);
    }
    
}
