package exceptions.fichier_config;

import exceptions.LireDonneesException;

public class ConfigNomObjetNonUniqueException extends LireDonneesException {

    public ConfigNomObjetNonUniqueException(String nomObjet, int numLigne, String nomFichierEntree) {
        super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierEntree + " a déjà été répertorié");
	codeErreur = 11 ;
    }
    
}
