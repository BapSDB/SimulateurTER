
package ihm;

import java.io.File;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

public class GestionArbreRepertoires {
    
    static final TreeView<String> ARBRE_REPERTOIRES = new TreeView<>() ;
    
    public static void createTree(File file, CheckBoxTreeItem<String> parent) {
        if (file.isDirectory()) {
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        } else {
            parent.getChildren().add(new CheckBoxTreeItem<>(file.getName()));
        }
    }
    
    public static void displayTreeView(String inputDirectoryLocation) {
        
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(inputDirectoryLocation);

        // Hides the root item of the tree view.
        ARBRE_REPERTOIRES.setShowRoot(false);

        // Creates the cell factory.
        ARBRE_REPERTOIRES.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

        // Get a list of files.
        File fileInputDirectoryLocation = new File(inputDirectoryLocation);
        File fileList[] = fileInputDirectoryLocation.listFiles();

        // create tree
        for (File file : fileList) {
            createTree(file, rootItem);
        }

        ARBRE_REPERTOIRES.setRoot(rootItem);
    }
    
}
