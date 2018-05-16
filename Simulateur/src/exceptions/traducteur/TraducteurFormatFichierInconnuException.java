
package exceptions.traducteur;

import exceptions.SimulateurException;

public class TraducteurFormatFichierInconnuException extends SimulateurException {

    public TraducteurFormatFichierInconnuException(String nomFichierOriginal) {
	super("Le fichier " + nomFichierOriginal + " n'a pas un format de fichier de traces reconnu.");
	codeErreur = 16 ;
    }
    
}
