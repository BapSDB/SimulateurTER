
package exceptions.config;

import exceptions.SimulateurException;


public class ConfigEcrireObjetsException extends SimulateurException {
    public ConfigEcrireObjetsException(String nomFichierConfig) {
	super("Une erreur est apparue lors de l'écriture des noms des objets dans le fichier de configuration " + nomFichierConfig) ;
	codeErreur = 9 ;
    };
}
