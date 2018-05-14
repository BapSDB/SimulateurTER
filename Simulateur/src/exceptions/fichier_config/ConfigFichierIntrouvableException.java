
package exceptions.fichier_config;

import exceptions.FichierIntrouvableException;

public class ConfigFichierIntrouvableException extends FichierIntrouvableException {

    public ConfigFichierIntrouvableException(String nomFichierEntree) {
	super("\"Le fichier de configuration \"" + nomFichierEntree + "\" n'existe pas\"");
	codeErreur = 6 ;
    }
    
}
