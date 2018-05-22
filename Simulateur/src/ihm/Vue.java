
package ihm;

import static ihm.GestionArbreRepertoires.ARBRE_REPERTOIRES;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import simulateur.Simulateur;
import traducteur.FabriqueTraducteur;

public class Vue extends Application {
    
    static final Rectangle2D RECTANGLE = Screen.getPrimary().getBounds() ;
    static final BorderPane LEFT_BORDER_PANE = new BorderPane(ARBRE_REPERTOIRES);
    static final BorderPane RIGHT_BORDER_PANE = new BorderPane();
    static final VBox OPTIONS = new VBox() ;
    static final ScrollPane CONSOLE = new ScrollPane() ;
    static final SplitPane SPLIT_PANE_VERTICAL_RIGHT = new SplitPane(RIGHT_BORDER_PANE, CONSOLE);
    static final SplitPane SPLIT_PANE_VERTICAL_LEFT = new SplitPane(LEFT_BORDER_PANE, OPTIONS);
    static final SplitPane SPLIT_PANE_HORIZONTAL = new SplitPane(SPLIT_PANE_VERTICAL_LEFT, SPLIT_PANE_VERTICAL_RIGHT);
    static final Scene SCENE = new Scene(SPLIT_PANE_HORIZONTAL, RECTANGLE.getWidth(), RECTANGLE.getHeight());
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simulateur de traces") ;
        GestionArbreRepertoires.creerArbre("traces") ;
        SPLIT_PANE_VERTICAL_LEFT.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_RIGHT.setOrientation(Orientation.VERTICAL);
        SPLIT_PANE_VERTICAL_LEFT.setMaxWidth(RECTANGLE.getWidth()*.2);
        CONSOLE.setMaxHeight(RECTANGLE.getHeight()*.2);
        String [] args = new String[1];
        getParameters().getRaw().toArray(args);
        verifierArguments(args);
        Simulateur simulateur = new Simulateur(FabriqueTraducteur.nouvelleFabrique(args[0]).creer()) ;
        CONSOLE.setContent(new Text(simulateur.getTraducteur().getContenu().toString()));
        CONSOLE.setFitToWidth(true);
        CONSOLE.setFitToHeight(true);
        CONSOLE.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
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
