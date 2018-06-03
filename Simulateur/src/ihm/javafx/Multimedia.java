
package ihm.javafx;

import static ihm.javafx.Options.activerIntervalle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import static ihm.javafx.Vue.TABLEAU;
import static ihm.javafx.Vue.charger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import static ihm.javafx.Vue.iterateur;
import simulateur.Iterateur.Etat;
import static simulateur.Iterateur.etat;

final class Multimedia extends GridPane {
    
    static final ToggleButton CHARGER_FICHIER = new ToggleButton("Charger Fichier");
    
    private final ImageView [] IMAGES_BOUTON = 
    {
        new ImageView(new Image("ressources/recule.png")),
        new ImageView(new Image("ressources/lecture.png")),
        new ImageView(new Image("ressources/avance.png")),
        new ImageView(new Image("ressources/pause.png"))
    };
    
    private static final Button [] MULTIMEDIA = new Button[4] ;
    private static PropertyChangeListener ecouteurSimulateur ;
    
    public Multimedia() {
        
        int i = 0 ;
        for (ImageView imageView : IMAGES_BOUTON) {
            imageView.setFitWidth(24d);
            imageView.setFitHeight(24d);
            Button button = new Button(null, imageView) ;
            button.setOnMouseEntered(Vue::saisirMouseEventEntered);
            button.setOnMouseExited(Vue::saisirMouseEventExited);
            button.setId("multimedia");
            button.setDisable(true);
            setConstraints(button, i, 1);
            MULTIMEDIA[i++] = button ;
            getChildren().add(button);
        }
        
        setConstraints(MULTIMEDIA[3], 1, 1);
        getChildren().remove(3);
        
        CHARGER_FICHIER.setDisable(true);
        CHARGER_FICHIER.setFont(Font.font(16));
        CHARGER_FICHIER.setMaxWidth(Double.MAX_VALUE);
        setFillWidth(CHARGER_FICHIER, true);
        setConstraints(CHARGER_FICHIER, 0, 0, 3, 1) ;
        getChildren().add(CHARGER_FICHIER) ;
        CHARGER_FICHIER.setId("multimedia");
        
        setPadding(new Insets(10d));
        setAlignment(Pos.CENTER);
        CHARGER_FICHIER.setOnAction(this::saisirActionEventChargerFichier);
    }
    
    void ecouterIterateur () {
        if (ecouteurSimulateur != null)
            iterateur.removePropertyChangeListener(ecouteurSimulateur);
        for (Button button : MULTIMEDIA)
            button.setDisable(true);
        MULTIMEDIA[0].setOnAction(this::saisirActionEventPrecedent);
        MULTIMEDIA[1].setOnAction(this::saisirActionEventLecture);
        MULTIMEDIA[2].setOnAction(this::saisirActionEventSuivant);
        MULTIMEDIA[3].setOnAction(this::saisirActionEventPause);
        iterateur.addPropertyChangeListener(ecouteurSimulateur = this::ecouterSimulateur);
        iterateur.mettreAjourIteration();
    }
    
    void activerLanceur () {
        MULTIMEDIA[1].setDisable(false);
        MULTIMEDIA[3].setDisable(false);
        MULTIMEDIA[1].requestFocus();
    }
    
    void désactiverLanceur () {
        getChildren().set(1, MULTIMEDIA[1]);
        MULTIMEDIA[1].setDisable(true);
        MULTIMEDIA[3].setDisable(true);
    }
    
        
    private void saisirActionEventChargerFichier (ActionEvent event) {
        CHARGER_FICHIER.setSelected(true);
        CHARGER_FICHIER.setMouseTransparent(true);
        CHARGER_FICHIER.setFocusTraversable(false);
        activerIntervalle();
        charger(new TacheChargerFichier());
    }
    
    private void saisirActionEventPrecedent (ActionEvent event) {
        if (etat == Etat.LECTURE)
            iterateur.suspendre();
        else
            getChildren().set(1, MULTIMEDIA[1]);
        MULTIMEDIA[0].requestFocus();
        iterateur.precedent();
    }


    private void saisirActionEventLecture (ActionEvent event) {
        getChildren().set(1, MULTIMEDIA[3]);
        MULTIMEDIA[3].requestFocus();
        iterateur.lancer();
    }
    
    private void saisirActionEventSuivant (ActionEvent event) {
        if (etat == Etat.LECTURE)
            iterateur.suspendre();
        else
            getChildren().set(1, MULTIMEDIA[1]);
        MULTIMEDIA[2].requestFocus();
        iterateur.suivant();
    }

    private void saisirActionEventPause (ActionEvent event) {
        iterateur.suspendre();
        getChildren().set(1, MULTIMEDIA[1]);
        MULTIMEDIA[1].requestFocus();
    }
    
    
    @SuppressWarnings("unchecked")
    private void ecouterSimulateur(PropertyChangeEvent evt) {

        switch(evt.getPropertyName()) {

            case "aPrecedent" :

                MULTIMEDIA[0].setDisable(!iterateur.aPrecedent()) ;
                Platform.runLater(() -> {
                    if(!iterateur.aPrecedent())
                        MULTIMEDIA[2].requestFocus();
                });
                break ;

            case "precedent" :

                Platform.runLater(() -> {
                    TABLEAU.getItems().remove(TABLEAU.getItems().size()-1);
                    TABLEAU.scrollTo(TABLEAU.getItems().size()-1);
                });
                break ;

            case "suivant" :

                Platform.runLater(() -> {
                    TABLEAU.getItems().add(FXCollections.observableList((List<String>)evt.getNewValue()));
                    TABLEAU.scrollTo(TABLEAU.getItems().size()-1);
                });
                break ;

            case "aSuivant" :

                MULTIMEDIA[2].setDisable(!iterateur.aSuivant()) ;
                Platform.runLater(() -> {
                    if(!iterateur.aSuivant()) {
                        getChildren().set(1, MULTIMEDIA[1]);
                        MULTIMEDIA[0].requestFocus();
                    }
                });
                break ;
        }
    }
}