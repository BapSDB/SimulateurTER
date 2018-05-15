
package exceptions.timestamp;

import exceptions.TimeStampException;

public class TimeStampParseException extends TimeStampException {
    
    public TimeStampParseException(String nomFichierEntree, String timeStamp, int numLigne, int errorOffset) {
        super("une erreur de parsing du timestamp " + timeStamp + " est apparue au caractère " + errorOffset + " de la ligne n° " + numLigne + " du fichier " + nomFichierEntree + ".");
	codeErreur = 12 ;
    }
    
}