
package exceptions.timestamp;

import exceptions.TimeStampException;

public class TimeStampDateException extends TimeStampException {

    public TimeStampDateException(String nomFichierEntree, String timeStamp, int numLigne) {
	super("Le timestamp " + timeStamp + " de la ligne n° " + numLigne + " du fichier " + nomFichierEntree + " a une sate incorrecte.");
	codeErreur = 13 ;
    }
    
}
