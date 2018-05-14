
package exceptions.fichier_config;

import exceptions.TraiterFichierExceptions;

public class ConfigTraiterFichierExceptions extends TraiterFichierExceptions {
    
    public ConfigTraiterFichierExceptions(String nomFichierEntreeConfig) {
	super(new ConfigFichierIntrouvableException(nomFichierEntreeConfig), new ConfigLireObjetsException(nomFichierEntreeConfig));
    }
    
}
