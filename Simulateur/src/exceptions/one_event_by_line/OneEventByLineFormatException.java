package exceptions.one_event_by_line;

import exceptions.SimulateurException;

public class OneEventByLineFormatException extends SimulateurException {

    public OneEventByLineFormatException(int numLigne, String ligne, String nomFichierEntree) {
	super("La ligne n°" + numLigne + " " + "\"" + ligne + "\" du fichier " + nomFichierEntree + " ne respecte pas le format \"One-Event-By-Line\".");
	codeErreur = 2 ;
    }
    
}
