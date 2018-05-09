
package exceptions.one_event_by_line;

import exceptions.SimulateurException;
import java.io.IOException;

public class OneEventByLineEcrireFormatCSVException extends SimulateurException {

    public OneEventByLineEcrireFormatCSVException(IOException ex, String nomFichierSortie) {
	super("Une erreur est apparue lors de l'écriture des traces au format CSV dans le fichier " + nomFichierSortie + ".", ex) ;
	codeErreur = 4 ;
    }

}
