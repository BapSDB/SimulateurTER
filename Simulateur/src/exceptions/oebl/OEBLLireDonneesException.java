
package exceptions.oebl;

import exceptions.SimulateurException;

public class OEBLLireDonneesException extends SimulateurException {
    
    public OEBLLireDonneesException(String nomFichierOEBL) {
	super("Une erreur est apparue lors de la lecture des données du fichier \"One-Event-By-Line\" " + nomFichierOEBL);
	codeErreur = 7 ;
    }
}
