
package exceptions.config;

import exceptions.EntreeSortieException;

public class ConfigLireObjetsException extends EntreeSortieException {
    
    public ConfigLireObjetsException(String nomFichierConfig) {
	super("Une erreur est apparue lors de la lecture des noms des objets dans le fichier de configuration " + nomFichierConfig + ".", null, null, nomFichierConfig) ;
	codeErreur = 8 ;
    };
	
}
