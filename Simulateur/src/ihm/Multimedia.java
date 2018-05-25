
package ihm;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public final class Multimedia extends HBox {
    
    private enum type {
        
        RECULE(0), LECTURE(1), PAUSE(2), AVANCE(3);
        
        private final int pos ;
        
        private type(int pos) {
            this.pos = pos ;
        }
        
        public int getPos() {
            return pos ;
        }
        
    }
    
    private final ImageView [] IMAGES_BOUTON = 
    {
        new ImageView(new Image("ressources/recule.png")),
        new ImageView(new Image("ressources/lecture.png")),
        new ImageView(new Image("ressources/pause.png")),
        new ImageView(new Image("ressources/avance.png"))
    };
    
    private final Button [] BOUTONS = new Button[4] ;
    
    
    public Multimedia() {
        int i = 0 ;
        for (ImageView imageView : IMAGES_BOUTON) {
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(24d);
            imageView.setFitHeight(24d);
            (BOUTONS[i++] = new Button(null, imageView)).setId("multimedia");
        }
        
        setSpacing(2d);
        setPadding(new Insets(10d));
        setAlignment(Pos.CENTER);
        getChildren().addAll(BOUTONS[type.RECULE.getPos()], BOUTONS[type.LECTURE.getPos()], BOUTONS[type.AVANCE.getPos()]);
        BOUTONS[type.LECTURE.getPos()].setOnAction((ActionEvent event) -> {
            getChildren().set(1, BOUTONS[type.PAUSE.getPos()]);
            requestFocus();
        });
        BOUTONS[type.PAUSE.getPos()].setOnAction((ActionEvent event) -> {
            getChildren().set(1, BOUTONS[type.LECTURE.getPos()]);
            requestFocus();
        });
    }
    
}
