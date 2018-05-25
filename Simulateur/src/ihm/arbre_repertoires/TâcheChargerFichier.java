
package ihm.arbre_repertoires;

import exceptions.SimulateurException;
import static ihm.Vue.ARBRE_REPERTOIRES;
import static ihm.Vue.BORDER_PANE_DROIT;
import static ihm.Vue.simulateur;
import static ihm.Vue.tableau;
import ihm.VueTableau;
import static ihm.arbre_repertoires.ObtenirNomFichier.obtenirNomFichier;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import simulateur.Simulateur;
import traducteur.FabriqueTraducteur;

class TâcheChargerFichier extends Task<Void> {
    
    private final TreeItem<String> treeItem ;

    public TâcheChargerFichier(TreeItem<String> treeItem) {
        this.treeItem = treeItem;
    }

    @Override
    protected Void call() throws Exception {
        try {

            simulateur = new Simulateur(FabriqueTraducteur.nouvelleFabrique(obtenirNomFichier(treeItem)).creer()) ;
            tableau = new VueTableau(simulateur.getEntete(), simulateur.getContenu().toString()) ;

            Platform.runLater(() -> {
                BORDER_PANE_DROIT.setCenter(tableau);
                ARBRE_REPERTOIRES.setDisable(false);
            });

        } catch (SimulateurException ex) {
            ex.afficherMessageDansConsole();
        }

        return null ;
    }

}
