
package exceptions.one_event_by_line;

import exceptions.SimulateurException;

public class OneEventByLineNomObjetIntrouvableException extends SimulateurException {
    public OneEventByLineNomObjetIntrouvableException(String nomObjet, int numLigne, String nomFichierEntree) {
	super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " n'existe pas dans l'environnement");
	codeErreur = 4 ;
    }
}
