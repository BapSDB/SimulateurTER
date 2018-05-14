package exceptions.one_event_by_line;

import exceptions.FichierIntrouvableException;

public class OneEventByLineFichierIntrouvableException extends FichierIntrouvableException {

    public OneEventByLineFichierIntrouvableException(String nomFichierEntree) {
	super("\"Le fichier \"one-event-by-line\" \"" + nomFichierEntree + "\" n'existe pas\"");
	codeErreur = 1 ;
    }
    
}