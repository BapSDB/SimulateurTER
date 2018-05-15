
package exceptions.fichier_config;

import exceptions.EntreeSortieException;

public class ConfigEcrireObjetsException extends EntreeSortieException {
    public ConfigEcrireObjetsException(String nomFichierSortie) {
	super("Une erreur est apparue lors de l'écriture des noms des objets dans le fichier de configuration " + nomFichierSortie + ".") ;
	codeErreur = 14 ;
    };
}
