package exceptions.one_event_by_line;

import exceptions.SimulateurException;
import java.io.FileNotFoundException;

public class OneEventByLineFichierIntrouvableException extends SimulateurException {

    public OneEventByLineFichierIntrouvableException(FileNotFoundException ex, String nomFichierEntree) {
	super("\"Le fichier \"one-event-by-line\" \"" + nomFichierEntree + "\" n'existe pas\"", ex);
	codeErreur = 1 ;
    }
    
}