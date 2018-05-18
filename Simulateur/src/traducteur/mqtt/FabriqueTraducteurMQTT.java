
package traducteur.mqtt;

import java.io.BufferedWriter;
import traducteur.FabriqueTraducteur;
import traducteur.Traducteur;
import util.TimeStamp;
import util.Util;
import static traducteur.mqtt.TraducteurMQTT.SEPARATEUR;

public class FabriqueTraducteurMQTT extends FabriqueTraducteur {
    
    public FabriqueTraducteurMQTT(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig, String nomFichierCSV) {
	
        super(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV);
	
	traduireLigne = (String ligne, String donnees, int numLigne, BufferedWriter config) -> {
	if (donnees != null) {
	    String [] strings = donnees.split(SEPARATEUR) ;
	    TimeStamp.verifierDate(Long.parseUnsignedLong(strings[0]), nomFichierOriginal, numLigne);
	    String timestamp = strings[0] ;
	    strings = strings[1].substring(1,strings[1].length()-1).split(",") ;
	    String nomObjet = (nomObjet = strings[1].split(":")[1]).substring(1, nomObjet.length()-1) ;
	    String valeur = (valeur = strings[3].split(":")[1]).substring(1, valeur.length()-1) ;
            ecrireNomObjet(nomObjet, config);
            lireValeur(timestamp, nomObjet, valeur);
	    return timestamp + Util.SEPARATEUR + nomObjet + Util.SEPARATEUR + valeur ;
	}
	return null ;
    } ;
	
    }

    @Override
    public Traducteur creer() {
	return new TraducteurMQTT(this) ;
    }
    
}
