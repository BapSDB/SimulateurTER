
package exceptions.timestamp;

import exceptions.SimulateurException;

public class TimeStampParseException extends SimulateurException {
    
    public TimeStampParseException(String nomFichierEntree, String timeStamp, int numLigne, int errorOffset) {
        super("une erreur de parsing du timestamp " + timeStamp + " est apparue au caractère " + errorOffset + " de la ligne n° " + numLigne + " du fichier " + nomFichierEntree + ".");
	codeErreur = 15 ;
    }
    
}