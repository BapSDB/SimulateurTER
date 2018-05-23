
package exceptions.config;

import exceptions.SimulateurException;

public class ConfigLireObjetsException extends SimulateurException {
    
    public ConfigLireObjetsException(String nomFichierConfig) {
	super("Une erreur est apparue lors de la lecture des noms des objets dans le fichier de configuration " + nomFichierConfig);
	codeErreur = 11 ;
    };
	
}
