
package ihm.javafx;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

    
class HandlerSourisEntrée implements EventHandler<MouseEvent> {

    private final Node node;

    public HandlerSourisEntrée(Node node) {
        this.node = node;
    }

    @Override
    public void handle(MouseEvent event) {
        node.setCursor(Cursor.HAND);
    }
}

class HandlerSourisSortie implements EventHandler<MouseEvent> {

    private final Node node;

    public HandlerSourisSortie(Node node) {
        this.node = node;
    }

    @Override
    public void handle(MouseEvent event) {
        node.setCursor(Cursor.DEFAULT);
    }
}
