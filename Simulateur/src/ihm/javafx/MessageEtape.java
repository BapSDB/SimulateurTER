
package ihm.javafx;

import static ihm.javafx.Vue.BORDER_PANE_ARBRE;
import static ihm.javafx.Vue.BORDER_PANE_TABLEAU;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.AnchorPane.setBottomAnchor;
import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setTopAnchor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


class MessageEtape {
    
    private static final ImageView FLECHE_VIEW = new ImageView() ;
    private static final Text MESSAGE = new Text() ;
    private static final VBox V_BOX = new VBox();
    private static final double LARGEUR = BORDER_PANE_ARBRE.getWidth() ;
    private static final double HAUTEUR = BORDER_PANE_ARBRE.getHeight()*.25d ;
    
    private static void setMessage(String string) {
        MESSAGE.setText(string);
        V_BOX.getChildren().clear();
        V_BOX.getChildren().addAll(MESSAGE, FLECHE_VIEW);
        FLECHE_VIEW.setFitWidth(LARGEUR);
        FLECHE_VIEW.setFitHeight(HAUTEUR);
        MESSAGE.setFont(Font.font(16d));
        V_BOX.setSpacing(10d);
    }
        
    public static void setMessageEtape1() {
        BORDER_PANE_TABLEAU.setCenter(new MessageEtape1("Veuillez sélectionner un fichier dans l'arbre des répertoires"));
    }

    public static void setMessageEtape2() {
        BORDER_PANE_TABLEAU.setCenter(new MessageEtape2("Cochez l'option désirée puis cliquez sur le bouton \"Charger Fichier\" pour en charger le contenu"));
    }

    private final static class MessageEtape1 extends AnchorPane {

        private static final Image FLECHE = new Image("ressources/fleche.png") ;

        public MessageEtape1(String message) {
            FLECHE_VIEW.setImage(FLECHE);
            setMessage(message);
            setTopAnchor(V_BOX, HAUTEUR);
            setLeftAnchor(V_BOX, LARGEUR);
            getChildren().add(V_BOX);
        }

    }

    private final static class MessageEtape2 extends AnchorPane {

        private static final Image FLECHE = new Image("ressources/fleche_oblique.png") ;
       
        public MessageEtape2(String message) {
            FLECHE_VIEW.setImage(FLECHE);
            setMessage(message);
            setBottomAnchor(V_BOX, HAUTEUR*.5d);
            setLeftAnchor(V_BOX, LARGEUR*.5d);
            getChildren().add(V_BOX);
        }

    }

}
