package ihm.javafx;

import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class VueTableau extends TableView<ObservableList<String>> {
    
    public void vider() {
        getColumns().clear();
        getItems().clear();
    }
    
    static class FabriqueCellule implements Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>> {

        @Override
        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
            TextFieldTableCell<ObservableList<String>, String> textFieldTableCell = new TextFieldTableCell<>() ;
            textFieldTableCell.setAlignment(Pos.CENTER);
            return textFieldTableCell ;
        }
    }

    static class FabriqueValeur implements Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>> {

        private final int index;

        public FabriqueValeur(int index) {
            this.index = index;
        }

        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
            return new ReadOnlyStringWrapper(param.getValue().get(index)) ;
        }
    }
    
    
    
}
