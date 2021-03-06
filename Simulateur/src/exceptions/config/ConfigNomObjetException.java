package exceptions.config;

import exceptions.LireDonneesException;

public class ConfigNomObjetException extends LireDonneesException {
    
    public ConfigNomObjetException(String nomObjet, int numLigne, String nomFichierEntree) {
	super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " ne respecte pas la convention de nommage.");
	codeErreur = 7 ;
    }
}
