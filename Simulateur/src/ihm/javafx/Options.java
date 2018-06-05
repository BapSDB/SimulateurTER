
package ihm.javafx;

import static ihm.javafx.Multimedia.CHARGER_FICHIER;
import ihm.javafx.Vue.ChargerToutLeFichier;
import static ihm.javafx.Vue.MULTIMEDIA;
import static ihm.javafx.Vue.TABLEAU;
import static ihm.javafx.Vue.afficherMessage;
import static ihm.javafx.Vue.charger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import static ihm.javafx.Vue.iterateur;

public final class Options extends VBox {
    
    private class Option extends RadioButton {
        
        private final int indice ;

        public Option(int indice) {
            this.indice = indice;
        }
        
    }
    
    private static final Option [] OPTIONS = new Option[4] ;
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
            OPTIONS[i].setOnMouseEntered(Vue::saisirMouseEventEntered);
            OPTIONS[i].setOnMouseExited(Vue::saisirMouseEventExited);
            
        }
        OPTIONS[0].setText("Vider la table");
        OPTIONS[1].setText("Afficher tout le contenu");
        OPTIONS[1].setSelected(true);
        OPTIONS[2].setText("Simuler la trace");
        OPTIONS[3].setGraphic(INTERVALLE);
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
        INTERVALLE.setDisable(!CHARGER_FICHIER.isSelected() || !OPTIONS[3].isSelected());
    }
    
    private static void ecouterOptions (Observable observable) {
        if(CHARGER_FICHIER.isSelected()) {
            iterateur.tuer() ;
            iterateur = iterateur.nouvelIterateur(indiceOptionSelectionnée.get());
            ecouterOptions();
            switch(indiceOptionSelectionnée.get()) {
                case 0 :
                    Platform.runLater(MULTIMEDIA::désactiverLanceur);
                    iterateur.setIterateur(iterateur.getContenu().listIterator());
                    TABLEAU.getItems().clear();
                    Platform.runLater(iterateur::mettreAjourIteration);
                    Platform.runLater(TABLEAU::afficherLignesVides);
                    afficherMessage("Le contenu de la table a été vidé");
                    break;
                case 1 :
                    Platform.runLater(MULTIMEDIA::désactiverLanceur);
                    charger(new ChargerToutLeFichier());
                    afficherMessage("La table contient toutes les données");
                    break ;
                case 2 :
                    Platform.runLater(MULTIMEDIA::activerLanceur);
                    break ;
                case 3 :
                    INTERVALLE.requestFocus();
            }
        }
    }

    private void ecouterRadioBoutons (Observable observable) {
        indiceOptionSelectionnée.setValue(getIndiceRadioBoutonSelectionné());
        INTERVALLE.setDisable(indiceOptionSelectionnée.get() != 3 || !CHARGER_FICHIER.isSelected());
    }

}
