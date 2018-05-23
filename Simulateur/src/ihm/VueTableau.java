package ihm;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class VueTableau extends TableView<ObservableList<String>> {

    public VueTableau() {
    }
    
    public VueTableau(String [] entete, String contenu) {
        TableColumn<ObservableList<String>, String> colonne ;
        int i = 0 ;
        for (String donnee : entete) {
            final int index = i++ ;
            getColumns().add(colonne = new TableColumn<>(donnee)) ;
            colonne.setCellValueFactory((param) -> {
                return new ReadOnlyStringWrapper(param.getValue().get(index)) ;
            });
            colonne.setCellFactory(new CallbackImpl());
            colonne.setMinWidth(colonne.getText().length()*10);
        }
        String ligne ;
        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(contenu.toCharArray()))) {
            while((ligne = bufferedReader.readLine()) != null) {
                getItems().add(FXCollections.observableArrayList(ligne.split(";")));
            }
        } catch (IOException ex) {
            Logger.getLogger(VueTableau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class CallbackImpl implements Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>> {

        @Override
        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
            TextFieldTableCell<ObservableList<String>, String> textFieldTableCell = new TextFieldTableCell<>() ;
            textFieldTableCell.setAlignment(Pos.CENTER);
            return textFieldTableCell ;
        }
    }
    
    
    
}
