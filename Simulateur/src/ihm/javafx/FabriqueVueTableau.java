
package ihm.javafx;

import static ihm.javafx.Vue.TABLEAU;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import simulateur.Conteneur;
import simulateur.Simulateur;
import traducteur.TableauCSV.ValeurPosition;
import traducteur.Traducteur;
import util.ListeChaineeOrdonnee;

public class FabriqueVueTableau {
    
    private final Simulateur simulateur ;
    private ObservableList<TableColumn<ObservableList<String>,String>> colonnes ;
    private ObservableList<ObservableList<String>> lignes ;
    
    public static FabriqueVueTableau nouvelleFabrique(Simulateur simulateur) {
        return new FabriqueVueTableau(simulateur) ;
    }
    
    private FabriqueVueTableau(Simulateur simulateur) {
        this.simulateur = simulateur;
    }
    
    public void creer () throws IOException {
        if (simulateur.getConteneur() != null)
            chargerFichier(simulateur.getConteneur());
        else
            chargerDonnees(simulateur.getTraducteur());
        TABLEAU.setDonnees(colonnes, lignes) ;
    }
    
    private void chargerEntete (Collection<String> entete) {
        TableColumn<ObservableList<String>, String> colonne ;
        int i = 0 ;
        for (String donnee : entete) {
            colonnes.add(colonne = new TableColumn<>(donnee)) ;
            colonne.setCellValueFactory(new FabriqueValeur(i++));
            colonne.setCellFactory(new FabriqueCellule());
            colonne.setMinWidth(colonne.getText().length()*10);
        }
    }
    
    private void chargerContenu (String contenu) throws IOException {
        String ligne ;
        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(contenu.toCharArray()))) {
            while((ligne = bufferedReader.readLine()) != null) {
                lignes.add(FXCollections.observableArrayList(ligne.split(";")));
            }
        }
    }
    
    private void chargerContenu (Map<String, ListeChaineeOrdonnee<ValeurPosition>> contenu) throws IOException {
        
        contenu.forEach((timestamp, donnees) -> {
            ObservableList<String> observableList = FXCollections.emptyObservableList() ;
            observableList.add(timestamp) ;
            for (ValeurPosition donnee : donnees)
                observableList.add(donnee.getValeur()) ;
            lignes.add(observableList);
        });
        
    }
    
    private void chargerFichier (Conteneur conteneur) throws IOException {
        chargerEntete(conteneur.getEntete());
        chargerContenu(conteneur.getContenu().toString());
    }
    
    private void chargerDonnees (Traducteur traducteur) throws IOException {
        chargerEntete(traducteur.getTableauCSV().getNomsObjets().keySet());
        chargerContenu(traducteur.getTableauCSV().getTableau());
    }
    
    private static class FabriqueValeur implements Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>> {

        private final int index;

        public FabriqueValeur(int index) {
            this.index = index;
        }

        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
            return new ReadOnlyStringWrapper(param.getValue().get(index)) ;
        }
    }

    private static class FabriqueCellule implements Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>> {

        @Override
        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
            TextFieldTableCell<ObservableList<String>, String> textFieldTableCell = new TextFieldTableCell<>() ;
            textFieldTableCell.setAlignment(Pos.CENTER);
            return textFieldTableCell ;
        }
    }
}
