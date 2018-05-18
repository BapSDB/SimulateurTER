
package simulateur;

import configurateur.Configurateur;
import traducteur.Traducteur;

public class FabriqueSimulateur {
    
    Traducteur traducteur ;
    
    public FabriqueSimulateur(Configurateur configurateur) {
	
    }

    public FabriqueSimulateur(Traducteur traducteur) {
        this.traducteur = traducteur ;
    }
    
    public Simulateur creer () {
	return new Simulateur(this) ;
    }
}
