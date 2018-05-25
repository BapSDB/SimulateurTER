
package ihm.arbre_repertoires;

import java.io.File;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldTreeCell;

public final class ArbreRepertoires extends TreeView<String> {
    
    static CheckBox currentSelectedCheckbox ;

    public ArbreRepertoires(String nomRepertoireRacine) {
        creerArbre(nomRepertoireRacine);
    }
    
    @FunctionalInterface
    private interface CreerArbre {
        public void creerArbre (CreerArbre ca, File fichier, TreeItem<String> parent);
    }
    
    public void creerArbre(String nomRepertoireRacine) {
        CreerArbre creerArbre = new CreerArbreImpl();
        TreeItem<String> rootItem = new TreeItem<>(nomRepertoireRacine);
        setShowRoot(false);
        setCellFactory(TextFieldTreeCell.forTreeView()) ;
        File fileInputDirectoryLocation = new File(nomRepertoireRacine);
        File fileList[] = fileInputDirectoryLocation.listFiles();
        for (File file : fileList) {
            creerArbre.creerArbre(creerArbre, file, rootItem);
        }
        setRoot(rootItem);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private class CreerArbreImpl implements CreerArbre {

        @Override
        public void creerArbre(CreerArbre ca, File fichier, TreeItem<String> parent) {
            TreeItem<String> treeItem = new TreeItem<>(fichier.getName()) ;
            if (fichier.isDirectory()) {
                treeItem.setExpanded(true);
                for (File f : fichier.listFiles())
                    ca.creerArbre(ca, f, treeItem);
            }
            else {
                CheckBox checkBox = new CheckBox() ;
                treeItem.setGraphic(checkBox) ;
                checkBox.selectedProperty().bindBidirectional(checkBox.disableProperty());
                checkBox.selectedProperty().addListener(new EcouteurSelectionCheckBox(checkBox, treeItem));
            }
            parent.getChildren().add(treeItem);
        }

    }
    
}