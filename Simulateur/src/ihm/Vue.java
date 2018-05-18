
package ihm;

import static ihm.GestionArbreRepertoires.ARBRE_REPERTOIRES;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Vue extends Application {
    
    static final BorderPane BORDER_PANE = new BorderPane();
    static final SplitPane SPLIT_PANE = new SplitPane(ARBRE_REPERTOIRES, BORDER_PANE);
    static final Scene SCENE = new Scene(SPLIT_PANE, 640, 480);
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simulateur de traces");
        GestionArbreRepertoires.creerArbre("traces");
        BORDER_PANE.setCenter(ARBRE_REPERTOIRES);
        primaryStage.setScene(SCENE);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
