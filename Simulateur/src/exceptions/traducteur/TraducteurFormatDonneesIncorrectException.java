
package exceptions.traducteur;

import exceptions.SimulateurException;

public class TraducteurFormatDonneesIncorrectException extends SimulateurException {
    
    public TraducteurFormatDonneesIncorrectException(String ligne, int numLigne, String nomFichierEntree) {
	super("La ligne de données " + "\"" + ligne + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " ne respecte pas le format de données attendu.");
	codeErreur = 2 ;
    }
    
}
