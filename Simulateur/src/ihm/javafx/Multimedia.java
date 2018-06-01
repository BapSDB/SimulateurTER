
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
import static ihm.javafx.Vue.simulateur;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

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
            button.setOnMouseEntered(new HandlerSourisEntrée(button));
            button.setOnMouseExited(new HandlerSourisSortie(button));
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
        MULTIMEDIA[1].addEventHandler(ActionEvent.ACTION, this::saisirActionEventLecture);
        MULTIMEDIA[3].addEventHandler(ActionEvent.ACTION, this::saisirActionEventPause);
        
    }
    
    void ecouterSimulateur () {
        if (ecouteurSimulateur != null)
            simulateur.removePropertyChangeListener(ecouteurSimulateur);
        for (Button button : MULTIMEDIA)
            button.setDisable(true);
        MULTIMEDIA[0].setOnAction(event->{simulateur.precedent();});
        MULTIMEDIA[1].setOnAction(event->{simulateur.lancer();});
        MULTIMEDIA[2].setOnAction(event->{simulateur.suivant();});
        MULTIMEDIA[3].setOnAction(event->{simulateur.suspendre();});
        simulateur.addPropertyChangeListener(ecouteurSimulateur = this::ecouterSimulateur);
        simulateur.mettreAjourIteration();
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


    private void saisirActionEventLecture (ActionEvent event) {
        getChildren().set(1, MULTIMEDIA[3]);
        MULTIMEDIA[3].requestFocus();
    }

    private void saisirActionEventPause (ActionEvent event) {
        getChildren().set(1, MULTIMEDIA[1]);
        MULTIMEDIA[1].requestFocus();
    }
    
    
    @SuppressWarnings("unchecked")
    private void ecouterSimulateur(PropertyChangeEvent evt) {

        switch(evt.getPropertyName()) {

            case "aPrecedent" :

                MULTIMEDIA[0].setDisable(!simulateur.isaPrecedent()) ;
                break ;

            case "precedent" :

                Platform.runLater(() -> {
                    TABLEAU.getItems().remove(TABLEAU.getItems().size()-1);
                    TABLEAU.scrollTo(TABLEAU.getItems().size()-1);
                });
                break ;

            case "suivant" :


                Platform.runLater(() -> {
                    TABLEAU.getItems().add(FXCollections.observableList((List<String>)(evt.getNewValue())));
                    TABLEAU.scrollTo(TABLEAU.getItems().size()-1);
                });
                break ;

            case "aSuivant" :

                MULTIMEDIA[2].setDisable(!simulateur.isaSuivant()) ;
                break ;
        }
    }
}