
package exceptions.traducteur;

import exceptions.FichierIntrouvableException;

public class TraducteurFichierIntrouvableException extends FichierIntrouvableException {

    public TraducteurFichierIntrouvableException(String nomFichierEntree) {
	super("\"Le fichier à traduire \"" + nomFichierEntree + "\" n'existe pas\"");
	codeErreur = 13 ;
    }
    
}
