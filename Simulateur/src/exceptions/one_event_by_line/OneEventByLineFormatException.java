package exceptions.one_event_by_line;

import exceptions.LireDonneesException;

public class OneEventByLineFormatException extends LireDonneesException {

    public OneEventByLineFormatException(String ligne, int numLigne, String nomFichierEntree) {
	super("La ligne n°" + numLigne + " " + "\"" + ligne + "\" du fichier " + nomFichierEntree + " ne respecte pas le format \"One-Event-By-Line\".");
	codeErreur = 2 ;
    }
    
}
