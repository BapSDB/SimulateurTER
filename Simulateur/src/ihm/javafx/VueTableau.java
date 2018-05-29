package ihm.javafx;

import static ihm.javafx.Vue.BORDER_PANE_ARBRE;
import static ihm.javafx.Vue.BORDER_PANE_TABLEAU;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class VueTableau extends TableView<ObservableList<String>> {
    
    public void chargerFichier (List<String> entete, String contenu) {
        getColumns().clear();
        TableColumn<ObservableList<String>, String> colonne ;
        int i = 0 ;
        for (String donnee : entete) {
            final int index = i++ ;
            getColumns().add(colonne = new TableColumn<>(donnee)) ;
            colonne.setCellValueFactory((param) -> {
                return new ReadOnlyStringWrapper(param.getValue().get(index)) ;
            });
            colonne.setCellFactory(new CallbackImpl());
            colonne.setMinWidth(colonne.getText().length()*10);
        }
        String ligne ;
        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(contenu.toCharArray()))) {
            while((ligne = bufferedReader.readLine()) != null) {
                getItems().add(FXCollections.observableArrayList(ligne.split(";")));
            }
        } catch (IOException ex) {
            Logger.getLogger(VueTableau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setMessageEtape1() {
        BORDER_PANE_TABLEAU.setCenter(new MessageEtape1());
    }
    
    public void setMessageEtape2() {
        BORDER_PANE_TABLEAU.setCenter(new MessageEtape2());
    }

    private static class CallbackImpl implements Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>> {

        @Override
        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
            TextFieldTableCell<ObservableList<String>, String> textFieldTableCell = new TextFieldTableCell<>() ;
            textFieldTableCell.setAlignment(Pos.CENTER);
            return textFieldTableCell ;
        }
    }
    
    private final static class MessageEtape1 extends AnchorPane {
        
        private static final ImageView FLECHE = new ImageView(new Image("ressources/fleche.png")) ;
        private static final Text MESSAGE_ETAPE_1 = new Text("Veuillez sélectionner un fichier dans l'arbre des répertoires") ;
        private static final VBox V_BOX = new VBox(MESSAGE_ETAPE_1, FLECHE);
        private static final double LARGEUR = BORDER_PANE_ARBRE.getWidth() ;
        private static final double HAUTEUR = BORDER_PANE_ARBRE.getHeight()*.25d ;

        public MessageEtape1() {
            FLECHE.setFitWidth(LARGEUR);
            FLECHE.setFitHeight(HAUTEUR);
            MESSAGE_ETAPE_1.setFont(Font.font(16d));
            V_BOX.setSpacing(10d);
            setTopAnchor(V_BOX, HAUTEUR);
            setLeftAnchor(V_BOX, LARGEUR);
            getChildren().add(V_BOX);
        }
        
    }
    
    private final static class MessageEtape2 extends AnchorPane {
        
        private static final ImageView FLECHE = new ImageView(new Image("ressources/fleche_oblique.png")) ;
        private static final Text MESSAGE_ETAPE_2 = new Text("Cochez l'option désirée puis cliquez sur le bouton \"Charger Fichier\" pour en charger le contenu") ;
        private static final VBox V_BOX = new VBox(MESSAGE_ETAPE_2, FLECHE);
        private static final double LARGEUR = BORDER_PANE_ARBRE.getWidth() ;
        private static final double HAUTEUR = BORDER_PANE_ARBRE.getHeight()*.25d ;

        public MessageEtape2() {
            FLECHE.setFitWidth(LARGEUR);
            FLECHE.setFitHeight(HAUTEUR);
            MESSAGE_ETAPE_2.setFont(Font.font(16d));
            V_BOX.setSpacing(10d);
            setBottomAnchor(V_BOX, HAUTEUR*.5d);
            setLeftAnchor(V_BOX, LARGEUR*.5d);
            getChildren().add(V_BOX);
        }
        
    }
    
    
    
}
