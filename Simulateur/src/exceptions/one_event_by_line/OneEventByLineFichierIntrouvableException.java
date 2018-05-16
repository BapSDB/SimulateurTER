package exceptions.one_event_by_line;

import exceptions.FichierIntrouvableException;

public class OneEventByLineFichierIntrouvableException extends FichierIntrouvableException {

    public OneEventByLineFichierIntrouvableException(String nomFichierOEBL) {
	super("\"Le fichier \"one-event-by-line\" \"" + nomFichierOEBL + "\" n'existe pas\"", nomFichierOEBL);
	codeErreur = 1 ;
    }
    
}