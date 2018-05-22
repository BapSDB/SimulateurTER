
package exceptions.traducteur;

import exceptions.FichierIntrouvableException;

public class TraducteurFichierIntrouvableException extends FichierIntrouvableException {

    public TraducteurFichierIntrouvableException(String nomFichierOriginal) {
	super("Le fichier à traduire \"" + nomFichierOriginal + "\" n'existe pas.", nomFichierOriginal);
	codeErreur = 1 ;
    }
    
}
