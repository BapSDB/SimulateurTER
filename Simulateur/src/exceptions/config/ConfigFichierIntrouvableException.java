
package exceptions.config;

import exceptions.SimulateurException;

public class ConfigFichierIntrouvableException extends SimulateurException {

    public ConfigFichierIntrouvableException(String nomFichierConfig) {
	super("\"Le fichier de configuration \"" + nomFichierConfig + "\" n'existe pas\"");
	codeErreur = 10 ;
    }
    
}
