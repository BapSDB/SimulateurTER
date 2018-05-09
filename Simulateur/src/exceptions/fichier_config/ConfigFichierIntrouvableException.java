
package exceptions.fichier_config;

import exceptions.SimulateurException;
import java.io.FileNotFoundException;

public class ConfigFichierIntrouvableException extends SimulateurException {

    public ConfigFichierIntrouvableException(FileNotFoundException ex, String nomFichierEntree) {
	super("\"Le fichier de configuration \"" + nomFichierEntree + "\" n'existe pas\"", ex);
	codeErreur = 5 ;
    }
    
}
