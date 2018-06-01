
package ihm.javafx;

import static ihm.javafx.Multimedia.CHARGER_FICHIER;
import ihm.javafx.Vue.ChargerToutLeFichier;
import static ihm.javafx.Vue.MULTIMEDIA;
import static ihm.javafx.Vue.TABLEAU;
import static ihm.javafx.Vue.charger;
import static ihm.javafx.Vue.simulateur;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public final class Options extends VBox {
    
    private class Option extends RadioButton {
        
        private final int indice ;

        public Option(int indice) {
            this.indice = indice;
        }
        
    }
    
    private static final Option [] OPTIONS = new Option[3] ;
    private static final Intervalle INTERVALLE = new Intervalle() ;
    private static InvalidationListener ecouterOptions ;
    private static final ToggleGroup TOGGLE_GROUP_OPTIONS = new ToggleGroup() ;
    
    static IntegerProperty indiceOptionSelectionnée = new SimpleIntegerProperty(1) ;
    
    public static int getIndiceRadioBoutonSelectionné () {
        return ((Option)TOGGLE_GROUP_OPTIONS.getSelectedToggle()).indice ;
    }

    public Options() {
        for (int i = 0; i < OPTIONS.length; i++) {
            OPTIONS[i] = new Option(i) ;
            OPTIONS[i].setToggleGroup(TOGGLE_GROUP_OPTIONS);
            OPTIONS[i].setOnMouseEntered(new HandlerSourisEntrée(OPTIONS[i]));
            OPTIONS[i].setOnMouseExited(new HandlerSourisSortie(OPTIONS[i]));
            
        }
        OPTIONS[0].setText("Vider la table");
        OPTIONS[1].setText("Afficher tout le contenu");
        OPTIONS[1].setSelected(true);
        OPTIONS[2].setGraphic(INTERVALLE);
        getChildren().addAll(OPTIONS);
        setSpacing(5d);
        setPadding(new Insets(10d));
        TOGGLE_GROUP_OPTIONS.selectedToggleProperty().addListener(this::ecouterRadioBoutons);
        INTERVALLE.setDisable(true);
    }
    
    static void ecouterOptions () {
        if (ecouterOptions != null)
            TOGGLE_GROUP_OPTIONS.selectedToggleProperty().removeListener(ecouterOptions);
        TOGGLE_GROUP_OPTIONS.selectedToggleProperty().addListener(ecouterOptions = Options::ecouterOptions);
    }
    
    static void activerIntervalle () {
        INTERVALLE.setDisable(!CHARGER_FICHIER.isSelected() || !OPTIONS[2].isSelected());
    }
    
    private static void ecouterOptions (Observable observable) {
        if(CHARGER_FICHIER.isSelected()) {
            simulateur.tuerTache();
            switch(indiceOptionSelectionnée.get()) {
                case 0 :
                    MULTIMEDIA.désactiverLanceur();
                    simulateur.setIterateur(simulateur.getContenu().listIterator());
                    TABLEAU.getItems().clear();
                    Platform.runLater(simulateur::mettreAjourIteration);
                    Platform.runLater(TABLEAU::afficherLignesVides);
                    break;
                case 1 :
                    MULTIMEDIA.désactiverLanceur();
                    charger(new ChargerToutLeFichier());
                    break ;
                case 2 :
                    Vue.afficherMessage("LOL");
                    simulateur = simulateur.nouvelAjourneur() ;
                    ecouterOptions();
                    Platform.runLater(MULTIMEDIA::ecouterSimulateur);
                    break ;
            }
        }
    }

    private void ecouterRadioBoutons (Observable observable) {
        indiceOptionSelectionnée.setValue(getIndiceRadioBoutonSelectionné());
        INTERVALLE.setDisable(indiceOptionSelectionnée.get() != 2 || !CHARGER_FICHIER.isSelected());
    }

}
