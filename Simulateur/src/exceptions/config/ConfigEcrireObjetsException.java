
package exceptions.config;

import exceptions.EntreeSortieException;

public class ConfigEcrireObjetsException extends EntreeSortieException {
    public ConfigEcrireObjetsException(String nomFichierConfig) {
	super("Une erreur est apparue lors de l'écriture des noms des objets dans le fichier de configuration " + nomFichierConfig + ".", null, null, nomFichierConfig) ;
	codeErreur = 14 ;
    };
}
