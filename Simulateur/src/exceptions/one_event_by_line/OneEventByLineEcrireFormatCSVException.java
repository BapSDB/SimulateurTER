
package exceptions.one_event_by_line;

import exceptions.EntreeSortieException;

public class OneEventByLineEcrireFormatCSVException extends EntreeSortieException {

    public OneEventByLineEcrireFormatCSVException(String nomFichierSortie) {
	super("Une erreur est apparue lors de l'écriture des traces au format CSV dans le fichier " + nomFichierSortie + ".") ;
	codeErreur = 3 ;
    }

}
