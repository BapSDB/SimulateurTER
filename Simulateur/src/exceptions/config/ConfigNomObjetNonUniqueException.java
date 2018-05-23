package exceptions.config;

import exceptions.SimulateurException;

public class ConfigNomObjetNonUniqueException extends SimulateurException {

    public ConfigNomObjetNonUniqueException(String nomObjet, int numLigne, String nomFichierEntree) {
        super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " a déjà été répertorié");
	codeErreur = 13 ;
    }
    
}
