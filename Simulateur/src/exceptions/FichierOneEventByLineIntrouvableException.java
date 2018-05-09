package exceptions;

public class FichierOneEventByLineIntrouvableException extends SimulateurException {

    public FichierOneEventByLineIntrouvableException(String nomFichierEntree) {
	codeErreur = 1 ;
	message = "\"Le fichier \"one-event-by-line\" \"" + nomFichierEntree + "\" n'existe pas\"" ;
    }
    
}