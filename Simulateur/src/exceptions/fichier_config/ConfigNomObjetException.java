package exceptions.fichier_config;

import exceptions.SimulateurException;

public class ConfigNomObjetException extends SimulateurException {
    
    public ConfigNomObjetException(String nomObjet, int numLigne, String nomFichierEntree) {
	super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " ne respecte pas la convention de nommage.");
	codeErreur = 5 ;
    }
}
