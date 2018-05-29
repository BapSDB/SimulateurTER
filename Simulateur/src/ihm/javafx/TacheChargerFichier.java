
package ihm.javafx;

import exceptions.SimulateurException;
import static ihm.javafx.Vue.ARBRE_REPERTOIRES;
import static ihm.javafx.Vue.fichierSélectionné;
import static ihm.javafx.Vue.simulateur;
import javafx.application.Platform;
import javafx.concurrent.Task;
import simulateur.Simulateur;
import traducteur.FabriqueTraducteur;
import static ihm.javafx.Vue.BORDER_PANE_TABLEAU;
import static ihm.javafx.Vue.BORDER_PANE_ARBRE;
import static ihm.javafx.Vue.BORDER_PANE_PANNEAU;
import static ihm.javafx.Vue.PANNEAU_DE_COMMANDES;
import static ihm.javafx.Vue.TABLEAU;

class TacheChargerFichier extends Task<Void> {
    
    @Override
    protected Void call() throws Exception {
        try {
            
            simulateur = new Simulateur(FabriqueTraducteur.nouvelleFabrique(fichierSélectionné).creer()) ;
            TABLEAU.chargerFichier(simulateur.getEntete(), simulateur.getContenu().toString()) ;
            
            Platform.runLater(() -> {
                BORDER_PANE_TABLEAU.setCenter(TABLEAU);
                BORDER_PANE_ARBRE.setCenter(ARBRE_REPERTOIRES);
                BORDER_PANE_PANNEAU.setCenter(PANNEAU_DE_COMMANDES);
            });

        } catch (SimulateurException ex) {
            ex.afficherMessageDansConsole();
        }

        return null ;
    }

}
