
package exceptions.one_event_by_line;

import exceptions.EntreeSortieException;

public class OneEventByLineLireDonneesException extends EntreeSortieException {
    
    public OneEventByLineLireDonneesException(String nomFichierEntree) {
	super("Une erreur est apparue lors de la lecture des données dans le fichier au format \"one-event-by-line\" " + nomFichierEntree + ".") ;
	codeErreur = 5 ;
    }
}
