package exceptions;

public class OneEventByLineFichierIntrouvableException extends SimulateurException {

    public OneEventByLineFichierIntrouvableException(String nomFichierEntree) {
	codeErreur = 1 ;
	message = "\"Le fichier \"one-event-by-line\" \"" + nomFichierEntree + "\" n'existe pas\"" ;
    }
    
}