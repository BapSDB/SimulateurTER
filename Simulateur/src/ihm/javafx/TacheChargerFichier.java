
package ihm.javafx;

import exceptions.SimulateurException;
import static ihm.javafx.Vue.ARBRE_REPERTOIRES;
import static ihm.javafx.Vue.fichierSélectionné;
import javafx.application.Platform;
import javafx.concurrent.Task;
import static ihm.javafx.Vue.BORDER_PANE_TABLEAU;
import static ihm.javafx.Vue.BORDER_PANE_ARBRE;
import static ihm.javafx.Vue.BORDER_PANE_PANNEAU;
import static ihm.javafx.Vue.PANNEAU_DE_COMMANDES;
import static ihm.javafx.Vue.TABLEAU;
import ihm.javafx.VueTableau.FabriqueCellule;
import ihm.javafx.VueTableau.FabriqueValeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import static simulateur.Formateur.nouveauFormateur;
import strategie.Simulateur;
import static traducteur.FabriqueTraducteur.nouvelleFabrique;

class TacheChargerFichier extends Task<Void> {

    private final int indice;

    TacheChargerFichier(int indice) {
        this.indice = indice;
    }
    
    @Override
    protected Void call() throws Exception {
        try {
            Simulateur simulateur = nouveauFormateur(nouvelleFabrique(fichierSélectionné).creer()).nouveauSimulateur() ;
            
            TABLEAU.vider() ;
            
            chargerDonnees(simulateur);

            Platform.runLater(() -> {
                BORDER_PANE_TABLEAU.setCenter(TABLEAU);
                BORDER_PANE_ARBRE.setCenter(ARBRE_REPERTOIRES);
                BORDER_PANE_PANNEAU.setCenter(PANNEAU_DE_COMMANDES);
                TABLEAU.scrollTo(0);
            });
            
        } catch (SimulateurException ex) {
            ex.afficherMessageDansConsole();
        }

        return null ;
    }
    
    private void chargerDonnees(Simulateur simulateur) {

        TableColumn<ObservableList<String>, String> colonne ;
        int i = 0 ;
        for (String donnee : simulateur.getEntete()) {
            TABLEAU.getColumns().add(colonne = new TableColumn<>(donnee)) ;
            colonne.setCellValueFactory(new FabriqueValeur(i++));
            colonne.setCellFactory(new FabriqueCellule());
            colonne.setMinWidth(colonne.getText().length()*10);
        }

        if (indice == 0)
            simulateur.getContenu().forEach(donnees -> {
                TABLEAU.getItems().add(FXCollections.observableArrayList(donnees));
            });
        

    }
    
}
