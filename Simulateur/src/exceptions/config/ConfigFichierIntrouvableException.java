
package exceptions.config;

import exceptions.FichierIntrouvableException;

public class ConfigFichierIntrouvableException extends FichierIntrouvableException {

    public ConfigFichierIntrouvableException(String nomFichierConfig) {
	super("\"Le fichier de configuration \"" + nomFichierConfig + "\" n'existe pas\"", nomFichierConfig);
	codeErreur = 6 ;
    }
    
}
