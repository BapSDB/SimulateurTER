
package ihm.javafx;

import static ihm.javafx.Vue.MULTIMEDIA;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javax.management.timer.Timer;
import static simulateur.Simulateur.intervalle;
import static util.TimeStamp.FORMAT_HMS;
import static util.TimeStamp.convertirFormatHMSVersLong;


final class Intervalle extends ComboBox<Intervalle.DureeIntervalle> {
    
    static class DureeIntervalle {
        private final String duree ;
        private final String hms ;
        private final long intervalle ;

        public DureeIntervalle(String duree, String hms, long intervalle) {
            this.duree = duree ;
            this.hms = hms ;
            this.intervalle = intervalle ;
        }

        @Override
        public String toString() {
            return hms ;
        }

    }
    
    private static final ObservableList<DureeIntervalle> LIST = FXCollections.observableArrayList(
            new DureeIntervalle("1 seconde","00:00:01", Timer.ONE_SECOND),
            new DureeIntervalle("1 minute","00:01:00", Timer.ONE_MINUTE),
            new DureeIntervalle("1 heure","01:00:00", Timer.ONE_HOUR)
    );
        
    private String hms, entree ;
    private static final String PROMP_TEXT = "Choisir un intervalle : hh:mm:ss" ;
    private static final Background RED_BACKGROUND = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)) ;
    private static final Background GREEN_BACKGROUND = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)) ;
    private static final Background WHITE_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)) ;

    public Intervalle() {
        super(LIST);
        setCellFactory(this::fabriquerCellule);
        setWidth(Double.MAX_VALUE);
        getEditor().setFont(Font.font(11d));
        setEditable(true);
        getEditor().setFocusTraversable(true);
        addEventFilter(KeyEvent.KEY_RELEASED, this::filtrerToucheEntreeRelachee);
        getEditor().textProperty().addListener(this::ecouterTextProperty);
        valueProperty().addListener(this::ecouterValueProperty);
        disabledProperty().addListener(this::ecouterDisabledProperty);
    }
    
    private void filtrerToucheEntreeRelachee (KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if(intervalle > -1)
                setValue(new DureeIntervalle(null, hms, intervalle));
            else
                Platform.runLater(() -> {
                    getEditor().setText(entree);
                    getEditor().end();
                    Platform.runLater(() -> {
                        getEditor().setBackground(RED_BACKGROUND);
                    });
                });
        }
    }
    
    private void ecouterTextProperty (Observable observable) {
        long temps ;
        if (getEditor().getText().trim().isEmpty())
            Platform.runLater(() -> {
                getEditor().setBackground(WHITE_BACKGROUND);
            });
        else if((temps = convertirDureeVersLong(getEditor().getText())) == -1) {
            getEditor().setBackground(RED_BACKGROUND);
            entree = getEditor().getText();
        }
        else {
            getEditor().setBackground(GREEN_BACKGROUND);
            intervalle = temps ;
            hms = getEditor().getText();
        }
    }
    
    private void ecouterValueProperty (Observable observable) {
        Platform.runLater(() -> {
            Vue.AFFICHAGE_CONSOLE.getChildren().add(new Text("intervalle = " + intervalle + ", Value = " + getValue() + "\n"));
            getEditor().setBackground(WHITE_BACKGROUND);
            setMouseTransparent(true);
            MULTIMEDIA.activerLanceur();
        });
    }
    
    private void ecouterDisabledProperty (Observable observable) {
        boolean disabled ;
        if(disabled = isDisabled())
            setPromptText(hms != null ? hms : PROMP_TEXT);
        setEditable(!disabled);
        setMouseTransparent(false);
        getEditor().requestFocus();
    }
    
    private static long convertirDureeVersLong (String duree) {
        long intervalle ;
        if (duree.matches(FORMAT_HMS))
            intervalle = convertirFormatHMSVersLong(duree)*Timer.ONE_SECOND ;
        else
            try {
                intervalle = Long.parseUnsignedLong(duree) ;
            } catch (NumberFormatException numberFormatException) {
                intervalle = -1l ;
            }
        return intervalle ;
    }
    
    private ListCell<DureeIntervalle> fabriquerCellule (ListView<DureeIntervalle> param) {
        return new TextFieldListCell<>(new StringConverter<DureeIntervalle>(){
                @Override
                public String toString(DureeIntervalle object) {
                    return object.duree ;
                }
                @Override
                public DureeIntervalle fromString(String string) {
                    throw new UnsupportedOperationException();
                }
        });
    }
    
}