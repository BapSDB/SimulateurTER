
package ihm.javafx;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public final class Options extends VBox {
    
    private static final RadioButton [] OPTIONS = new RadioButton[4] ;
    private static final ToggleGroup TOGGLE_GROUP = new ToggleGroup();

    public Options() {
        for (int i = 0; i < OPTIONS.length; i++) {
            OPTIONS[i] = new RadioButton() ;
            OPTIONS[i].setToggleGroup(TOGGLE_GROUP);
            OPTIONS[i].setOnMouseEntered(new HandlerSourisEntrée(OPTIONS[i]));
            OPTIONS[i].setOnMouseExited(new HandlerSourisSortie(OPTIONS[i]));
            
        }
        OPTIONS[0].setText("Afficher tout le contenu");
        OPTIONS[0].setSelected(true);
        OPTIONS[1].setText("Afficher ligne par ligne");
        OPTIONS[2].setText("Afficher une ligne toutes les minutes");
        OPTIONS[3].setText("Afficher une ligne toutes les 10 minutes");
        getChildren().addAll(OPTIONS);
        setSpacing(5d);
        setPadding(new Insets(10d));
    }
    
}
