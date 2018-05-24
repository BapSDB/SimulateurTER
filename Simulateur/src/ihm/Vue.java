
package ihm;

import javafx.application.Application;
import javafx.geometry.Orientation;
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
import static util.TimeStamp.getTimestampAffichageConsole;

public class Vue extends Application {
    
    private static final String MESSAGE_INTRODUCTION = "Bienvenue dans le simulateur de traces :\n"
            + "\t- Veuillez sélectionner un fichier de traces à charger en cochant l'une des cases de l'arbre des répertoires en haut à gauche de votre écran.\n"
            + "\t- Puis choissisez l'une des stratégies de simulation à appliquer.\n" ;
    static final Rectangle2D RECTANGLE = Screen.getPrimary().getBounds() ;
    static final ArbreRepertoires ARBRE_REPERTOIRES = new ArbreRepertoires("traces") ;
    static final BorderPane LEFT_BORDER_PANE = new BorderPane(ARBRE_REPERTOIRES);
    static VueTableau FICHIER ;
    static final BorderPane RIGHT_BORDER_PANE = new BorderPane();
    static final VBox OPTIONS = new VBox() ;
    static final TextFlow AFFICHAGE_CONSOLE = new TextFlow() ;
    static final ScrollPane CONSOLE = new ScrollPane() ;
    static final SplitPane SPLIT_PANE_VERTICAL_RIGHT = new SplitPane(RIGHT_BORDER_PANE, CONSOLE);
    static final SplitPane SPLIT_PANE_VERTICAL_LEFT = new SplitPane(LEFT_BORDER_PANE, OPTIONS);
    static final SplitPane SPLIT_PANE_HORIZONTAL = new SplitPane(SPLIT_PANE_VERTICAL_LEFT, SPLIT_PANE_VERTICAL_RIGHT);
    static final Scene SCENE = new Scene(SPLIT_PANE_HORIZONTAL, RECTANGLE.getWidth(), RECTANGLE.getHeight());
    static Simulateur SIMULATEUR ;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("Simulateur de traces") ;
        
        SPLIT_PANE_VERTICAL_LEFT.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_RIGHT.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_LEFT.setMaxWidth(RECTANGLE.getWidth()*.2);
        
        CONSOLE.setMaxHeight(RECTANGLE.getHeight()*.2);
        AFFICHAGE_CONSOLE.getChildren().add(new Text(getTimestampAffichageConsole()+MESSAGE_INTRODUCTION));
        CONSOLE.setContent(AFFICHAGE_CONSOLE);
        CONSOLE.setFitToWidth(true);
        CONSOLE.setFitToHeight(true);
        CONSOLE.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        CONSOLE.vvalueProperty().bind(AFFICHAGE_CONSOLE.heightProperty());
        
        FICHIER = new VueTableau() ;
        RIGHT_BORDER_PANE.setCenter(FICHIER);
        
        primaryStage.setScene(SCENE) ;
        primaryStage.show() ;
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
