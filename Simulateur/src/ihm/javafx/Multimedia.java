
package ihm.javafx;

import static ihm.javafx.Vue.BORDER_PANE_PANNEAU;
import ihm.javafx.HandlerSourisEntrée;
import ihm.javafx.HandlerSourisSortie;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import static ihm.javafx.Vue.BORDER_PANE_TABLEAU;
import static ihm.javafx.Vue.BORDER_PANE_ARBRE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public final class Multimedia extends GridPane {
    
    static final ToggleButton CHARGER_FICHIER = new ToggleButton("Charger Fichier");
    
    private final ImageView [] IMAGES_BOUTON = 
    {
        new ImageView(new Image("ressources/recule.png")),
        new ImageView(new Image("ressources/lecture.png")),
        new ImageView(new Image("ressources/avance.png")),
        new ImageView(new Image("ressources/pause.png"))
    };
    
    private static final Button [] MULTIMEDIA = new Button[4] ;
    
    public Multimedia() {
        
        int i = 0 ;
        for (ImageView imageView : IMAGES_BOUTON) {
            imageView.setFitWidth(24d);
            imageView.setFitHeight(24d);
            Button button = new Button(null, imageView) ;
            button.setOnMouseEntered(new HandlerSourisEntrée(button));
            button.setOnMouseExited(new HandlerSourisSortie(button));
            button.setId("multimedia");
            setConstraints(button, i, 1);
            MULTIMEDIA[i++] = button ;
            getChildren().add(button);
        }
        
        setConstraints(MULTIMEDIA[3], 1, 1);
        getChildren().remove(3);
        
        CHARGER_FICHIER.setSelected(true);
        CHARGER_FICHIER.setMouseTransparent(true);
        CHARGER_FICHIER.setFont(Font.font(16));
        CHARGER_FICHIER.setMaxWidth(Double.MAX_VALUE);
        setFillWidth(CHARGER_FICHIER, true);
        setConstraints(CHARGER_FICHIER, 0, 0, 3, 1) ;
        getChildren().add(CHARGER_FICHIER) ;
        CHARGER_FICHIER.setId("multimedia");
        
        setPadding(new Insets(10d));
        setAlignment(Pos.CENTER);
        CHARGER_FICHIER.setOnAction(new HandlerChargerFichier());
        MULTIMEDIA[1].setOnAction(new HandlerLecture());
        MULTIMEDIA[3].setOnAction(new HandlerPause());
    }

    private static class HandlerChargerFichier implements EventHandler<ActionEvent> {

        public HandlerChargerFichier() {
        }

        @Override
        public void handle(ActionEvent event) {
            if (CHARGER_FICHIER.isSelected()) {
                
                CHARGER_FICHIER.setSelected(true);
                CHARGER_FICHIER.setMouseTransparent(true);
                ProgressIndicator progressIndicatorTableau = new ProgressIndicator() ;
                ProgressIndicator progressIndicatorArbre = new ProgressIndicator() ;
                ProgressIndicator progressIndicatorPanneau = new ProgressIndicator() ;
                TacheChargerFichier tâcheChargerFichier = new TacheChargerFichier() ;
                progressIndicatorTableau.progressProperty().bind(tâcheChargerFichier.progressProperty());
                progressIndicatorArbre.progressProperty().bind(tâcheChargerFichier.progressProperty());
                progressIndicatorPanneau.progressProperty().bind(tâcheChargerFichier.progressProperty());
                
                Platform.runLater(() -> {
                    BORDER_PANE_TABLEAU.setCenter(progressIndicatorTableau);
                    BORDER_PANE_ARBRE.setCenter(progressIndicatorArbre);
                    BORDER_PANE_PANNEAU.setCenter(progressIndicatorPanneau);
                });
                
                new Thread(tâcheChargerFichier).start();
                
            }
        }
    }

    private class HandlerLecture implements EventHandler<ActionEvent> {

        public HandlerLecture() {
        }

        @Override
        public void handle(ActionEvent event) {
            getChildren().set(1, MULTIMEDIA[3]);
        }
    }

    private class HandlerPause implements EventHandler<ActionEvent> {

        public HandlerPause() {
        }

        @Override
        public void handle(ActionEvent event) {
            getChildren().set(1, MULTIMEDIA[1]);
        }
    }
}