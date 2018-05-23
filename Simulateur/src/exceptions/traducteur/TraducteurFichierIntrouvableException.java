
package exceptions.traducteur;

import exceptions.SimulateurException;

public class TraducteurFichierIntrouvableException extends SimulateurException {

    public TraducteurFichierIntrouvableException(String nomFichierOriginal) {
	super("Le fichier à traduire \"" + nomFichierOriginal + "\" n'existe pas.");
	codeErreur = 1 ;
    }
    
}
