
package ihm.javafx;

import exceptions.SimulateurException;
import static ihm.javafx.MessageEtape.setMessageEtape2;
import static ihm.javafx.Multimedia.CHARGER_FICHIER;
import static ihm.javafx.Vue.fichierSélectionné;
import java.io.File;
import java.io.IOException;
import javafx.beans.Observable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

class Fichier extends HBox {
        
    private final File fichier ;
    private RadioButton radioButton = new RadioButton() ;
    private final ToggleGroup toggleGroup ;

    public Fichier(String nomFichier, ToggleGroup toggleGroup) {
        this(new File(nomFichier), toggleGroup) ;
    }

    public Fichier(File fichier, ToggleGroup toggleGroup) {
        this.fichier = fichier;
        this.toggleGroup = toggleGroup;
        configurerFichier();
    }

    private void configurerFichier() {
        Text text = new Text(fichier.getName());
        radioButton.setToggleGroup(toggleGroup);
        radioButton.selectedProperty().addListener(this::ecouterRadioButton);
        setOnMouseEntered(Vue::saisirMouseEventEntered);
        setOnMouseExited(Vue::saisirMouseEventExited);
        setOnMouseClicked(this::saisirMouseEventClicked);
        getChildren().addAll(radioButton, text) ;
        setSpacing(5d);
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }
    
    public void setRépertoire() {
        getChildren().remove(radioButton);
        setMouseTransparent(true);
        radioButton = null ;
    }

    void selectionerFichier () {
        try {
            fichierSélectionné = fichier.getCanonicalPath() ;
        } catch (IOException ex) {
            new CheminCanoniqueException().afficherMessageDansConsole() ;
        }
        CHARGER_FICHIER.setDisable(false);
        CHARGER_FICHIER.setSelected(false);
        CHARGER_FICHIER.setMouseTransparent(false);
        CHARGER_FICHIER.setFocusTraversable(true);
        setMessageEtape2();
    }
    
    private void saisirMouseEventClicked(MouseEvent event) {
        if (!radioButton.isSelected()) {
            radioButton.setSelected(true) ;
            selectionerFichier();
        }
    }
    
    private void ecouterRadioButton (Observable observable) {
        if(radioButton.isSelected())
            selectionerFichier();
    }

    private class CheminCanoniqueException extends SimulateurException {

        public CheminCanoniqueException() {
            super("Impossible de d'extraire le chemin absolu du fichier " + fichier.getName());
        }

    }
}
