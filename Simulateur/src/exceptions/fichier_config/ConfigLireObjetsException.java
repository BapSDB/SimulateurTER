
package exceptions.fichier_config;

import exceptions.EntreeSortieException;

public class ConfigLireObjetsException extends EntreeSortieException {
    
    public ConfigLireObjetsException(String nomFichierEntree) {
	super("Une erreur est apparue lors de la lecture des noms des objets dans le fichier de configuration " + nomFichierEntree + ".") ;
	codeErreur = 8 ;
    };
	
}
