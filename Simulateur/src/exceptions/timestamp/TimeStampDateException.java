
package exceptions.timestamp;

import exceptions.SimulateurException;


public class TimeStampDateException extends SimulateurException {

    public TimeStampDateException(String nomFichierEntree, String timeStamp, int numLigne) {
	super("Le timestamp " + timeStamp + " de la ligne n° " + numLigne + " du fichier " + nomFichierEntree + " a une sate incorrecte.");
	codeErreur = 14 ;
    }
    
}
