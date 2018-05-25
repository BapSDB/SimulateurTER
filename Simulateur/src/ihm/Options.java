
package ihm;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public final class Options extends VBox {
    
    private final RadioButton [] options = new RadioButton[4] ;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public Options() {
        for (int i = 0; i < options.length; i++) {
            options[i] = new RadioButton() ;
            options[i].setToggleGroup(toggleGroup);
            
        }
        options[0].setText("Afficher tout le contenu");
        options[1].setText("Afficher ligne par ligne");
        options[2].setText("Afficher une ligne toutes les minutes");
        options[3].setText("Afficher une ligne toutes les 10 minutes");
        getChildren().addAll(options);
        setSpacing(5d);
        setPadding(new Insets(10d));
    }
    
}
