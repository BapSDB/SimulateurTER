
package configurateur;

import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import traducteur.Traducteur;

public class FabriqueConfigurateur {
    
    Traducteur traducteur ;
    
    public FabriqueConfigurateur(Traducteur traducteur) {
	this.traducteur = traducteur ;
    }
    
    public Configurateur creer () throws ConfigFichierIntrouvableException, ConfigLireObjetsException {
	return new Configurateur(this) ;
    }
    
}
