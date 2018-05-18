
package ihm;

import java.io.File;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

public class GestionArbreRepertoires {
    
    static final TreeView<String> ARBRE_REPERTOIRES = new TreeView<>() ;
    
    @FunctionalInterface
    private interface CreerArbre {
        public void creerArbre (CreerArbre ca, File fichier, CheckBoxTreeItem<String> parent);
    }
    
    public static void creerArbre(String nomRepertoireRacine) {
        CreerArbre creerArbre = (ca, fichier, parent) -> {
            if (fichier.isDirectory()) {
                CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(fichier.getName());
                parent.getChildren().add(treeItem);
                for (File f : fichier.listFiles()) {
                    ca.creerArbre(ca, f, treeItem);
                }
            } else {
                parent.getChildren().add(new CheckBoxTreeItem<>(fichier.getName()));
            }
        };
        
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(nomRepertoireRacine);
        ARBRE_REPERTOIRES.setShowRoot(false);
        ARBRE_REPERTOIRES.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        File fileInputDirectoryLocation = new File(nomRepertoireRacine);
        File fileList[] = fileInputDirectoryLocation.listFiles();
        for (File file : fileList) {
            creerArbre.creerArbre(creerArbre, file, rootItem);
        }
        ARBRE_REPERTOIRES.setRoot(rootItem);
    }
}
