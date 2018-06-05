
package ihm.javafx;

import exceptions.SimulateurException;
import static ihm.javafx.Options.ecouterOptions;
import static ihm.javafx.Options.indiceOptionSelectionnée;
import static ihm.javafx.Vue.ARBRE_REPERTOIRES;
import ihm.javafx.Vue.ChargerToutLeFichier;
import static ihm.javafx.Vue.MULTIMEDIA;
import static ihm.javafx.Vue.fichierSélectionné;
import javafx.application.Platform;
import javafx.concurrent.Task;
import static ihm.javafx.Vue.TABLEAU;
import static ihm.javafx.Vue.charger;
import ihm.javafx.VueTableau.FabriqueCellule;
import ihm.javafx.VueTableau.FabriqueValeur;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import static simulateur.Formateur.nouveauFormateur;
import static traducteur.FabriqueTraducteur.nouvelleFabrique;
import static ihm.javafx.Vue.iterateur;

class TacheChargerFichier extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        try {
            
            iterateur = nouveauFormateur(nouvelleFabrique(fichierSélectionné).creer()).nouvelIterateur(indiceOptionSelectionnée.get()) ;
            ecouterOptions();
            Platform.runLater(MULTIMEDIA::ecouterIterateur);
            TABLEAU.vider() ;
            
            chargerDonnees();

            
        } catch (SimulateurException ex) {
            ex.afficherMessageDansConsole();
        }

        return null ;
    }
    
    private void chargerDonnees() {
        
        TableColumn<ObservableList<String>, String> colonne ;
        int i = 0 ;
        for (String donnee : iterateur.getEntete()) {
            TABLEAU.getColumns().add(colonne = new TableColumn<>(donnee)) ;
            colonne.setCellValueFactory(new FabriqueValeur(i++));
            colonne.setCellFactory(new FabriqueCellule());
            colonne.setMinWidth(colonne.getText().length()*10);
        }
        
        if (indiceOptionSelectionnée.get() == 1)
            charger(new ChargerToutLeFichier());
        else
            Platform.runLater(Vue::mettreAjourAffichageInterface);
        
        if (indiceOptionSelectionnée.get() == 2)
            MULTIMEDIA.activerLanceur() ;
        
        Platform.runLater(() -> {
            ARBRE_REPERTOIRES.creerArbre("traces");
        });

    }
}
