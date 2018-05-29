
package ihm.javafx;

import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

final class ArbreRepertoires extends TreeView<Fichier> {
    
    private static final ToggleGroup TOGGLE_GROUP = new ToggleGroup() ;
    
    public ArbreRepertoires(String nomRepertoireRacine) {
        creerArbre(nomRepertoireRacine);
    }
    
    @FunctionalInterface
    private interface CreerArbre {
        public void creerArbre (CreerArbre ca, File fichier, TreeItem<Fichier> parent);
    }
    
    public void creerArbre(String nomRepertoireRacine) {
        CreerArbre creerArbre = new CreerArbreImpl();
        TreeItem<Fichier> rootItem = new TreeItem<>(new Fichier(nomRepertoireRacine, TOGGLE_GROUP));
        rootItem.getValue().setRépertoire();
        setShowRoot(false);
        for (File file : new File(nomRepertoireRacine).listFiles()) {
            creerArbre.creerArbre(creerArbre, file, rootItem);
        }
        setRoot(rootItem);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setOnKeyPressed(new HandlerClavierSelectionFichier());
    }
    
    private class CreerArbreImpl implements CreerArbre {

        @Override
        public void creerArbre(CreerArbre ca, File fichier, TreeItem<Fichier> parent) {
            TreeItem<Fichier> treeItem = new TreeItem<>(new Fichier(fichier, TOGGLE_GROUP)) ;
            if (fichier.isDirectory()) {
                treeItem.setExpanded(true);
                treeItem.getValue().setRépertoire();
                for (File f : fichier.listFiles())
                    ca.creerArbre(ca, f, treeItem);
            }
            parent.getChildren().add(treeItem);
        }
    }
    
    private class HandlerClavierSelectionFichier implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            
            TreeItem<Fichier> treeItem = getSelectionModel().getSelectedItem() ;
            
            if (treeItem != null && treeItem.getValue().getRadioButton() != null && event.getCode() == KeyCode.ENTER) {
                treeItem.getValue().getRadioButton().setSelected(true) ;
                treeItem.getValue().selectionerFichier();
            }
        }
    }
}