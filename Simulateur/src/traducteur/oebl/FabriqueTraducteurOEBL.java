
package traducteur.oebl;

import traducteur.FabriqueTraducteur;
import traducteur.Traducteur;
import static util.Util.SEPARATEUR;

public class FabriqueTraducteurOEBL extends FabriqueTraducteur {

    public FabriqueTraducteurOEBL(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig, String nomFichierCSV) {
	super(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV);
        
        traduireLigne = (ligne, donnees, numLigne, config) -> {
            if (donnees != null) {
                String [] evenement = donnees.split(SEPARATEUR) ;
                tableauCSV.lireValeur(evenement[0], evenement[1], evenement[2]);
                return ligne ;
            }
            return null ;
        };
    }
    
    @Override
    public Traducteur creer() {
	return new TraducteurOEBL(this);
    }
    
}
