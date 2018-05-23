
package exceptions.oebl;

import exceptions.SimulateurException;

public class OEBLFichierIntrouvableException extends SimulateurException {

    public OEBLFichierIntrouvableException(String nomFichierOEBL) {
	super("Le fichier \"One-Event-By-Line\" " + nomFichierOEBL + " n'existe pas");
	codeErreur = 6 ;
    }

}
