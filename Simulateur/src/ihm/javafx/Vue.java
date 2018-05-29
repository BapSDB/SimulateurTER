
package ihm.javafx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import simulateur.Simulateur;
import static traducteur.Traducteur.AFFICHAGE_BEAN;
import static util.TimeStamp.getTimestampAffichageConsole;

public class Vue extends Application {
    
    static Simulateur simulateur ;
    static final VueTableau TABLEAU = new VueTableau() ;
    static final ArbreRepertoires ARBRE_REPERTOIRES = new ArbreRepertoires("traces") ;
    static final VBox PANNEAU_DE_COMMANDES = new VBox(new Multimedia(), new Options()) ;
    static final BorderPane BORDER_PANE_TABLEAU = new BorderPane(TABLEAU) ;
    static final BorderPane BORDER_PANE_ARBRE = new BorderPane(ARBRE_REPERTOIRES);
    static final BorderPane BORDER_PANE_PANNEAU = new BorderPane(PANNEAU_DE_COMMANDES) ;
    static String fichierSélectionné ;
    
    private static final String MESSAGE_INTRODUCTION = "Bienvenue dans le simulateur de traces :\n"
            + "\t- Veuillez sélectionner un fichier de traces à charger en cochant l'une des cases de l'arbre des répertoires en haut à gauche de votre écran.\n"
            + "\t- Puis choissisez l'une des stratégies de simulation à appliquer.\n" ;
    private static final Rectangle2D RECTANGLE = Screen.getPrimary().getBounds() ;
    private static final TextFlow AFFICHAGE_CONSOLE = new TextFlow() ;
    private static final ScrollPane CONSOLE = new ScrollPane() ;
    private static final SplitPane SPLIT_PANE_VERTICAL_DROIT = new SplitPane(BORDER_PANE_TABLEAU, CONSOLE);
    private static final SplitPane SPLIT_PANE_VERTICAL_GAUCHE = new SplitPane(BORDER_PANE_ARBRE, BORDER_PANE_PANNEAU);
    private static final SplitPane SPLIT_PANE_HORIZONTAL = new SplitPane(SPLIT_PANE_VERTICAL_GAUCHE, SPLIT_PANE_VERTICAL_DROIT);
    private static final Scene SCENE = new Scene(SPLIT_PANE_HORIZONTAL, RECTANGLE.getWidth(), RECTANGLE.getHeight());
    private static final EcouteurAffichage ECOUTEUR_AFFICHAGE = new EcouteurAffichage() ;
    
    private static class EcouteurAffichage implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Platform.runLater(() -> {
                AFFICHAGE_CONSOLE.getChildren().add(new Text(getTimestampAffichageConsole() + evt.getNewValue().toString()));
            });
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("Simulateur de traces") ;
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest((event) -> {
            AFFICHAGE_BEAN.removePropertyChangeListener(ECOUTEUR_AFFICHAGE);
        });
        
        SCENE.getStylesheets().add("ressources/button.css");
        
        PANNEAU_DE_COMMANDES.setSpacing(10d);
        PANNEAU_DE_COMMANDES.setAlignment(Pos.CENTER);
        BORDER_PANE_PANNEAU.setMaxHeight(0);
        
        AFFICHAGE_BEAN.addPropertyChangeListener(ECOUTEUR_AFFICHAGE);
        
        SPLIT_PANE_VERTICAL_GAUCHE.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_DROIT.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_GAUCHE.setMaxWidth(RECTANGLE.getWidth()*.2);
        
        CONSOLE.setMaxHeight(RECTANGLE.getHeight()*.2);
        AFFICHAGE_CONSOLE.getChildren().add(new Text(getTimestampAffichageConsole()+MESSAGE_INTRODUCTION));
        CONSOLE.setContent(AFFICHAGE_CONSOLE);
        CONSOLE.setFitToWidth(true);
        CONSOLE.setFitToHeight(true);
        CONSOLE.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        CONSOLE.vvalueProperty().bind(AFFICHAGE_CONSOLE.heightProperty());
        
        primaryStage.setScene(SCENE) ;
        primaryStage.show() ;
        
        TABLEAU.setMessageEtape1();
        ARBRE_REPERTOIRES.requestFocus();
    }
    
    public void verifierArguments (String [] args) {
	if (args.length < 1) {
	    System.err.println("usage : java simulateur.Simulateur <nom_de_fichier_de_traces.(sw2|mqtt|oebl)");
	    System.exit(99);
	}
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
