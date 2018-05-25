/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.arbre_repertoires;

import static ihm.Vue.ARBRE_REPERTOIRES;
import static ihm.Vue.BORDER_PANE_DROIT;
import static ihm.arbre_repertoires.ArbreRepertoires.currentSelectedCheckbox;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import static ihm.Vue.fichierSélectionné;

class EcouteurSelectionCheckBox implements InvalidationListener {

    private final CheckBox checkBox;
    private final TreeItem<String> treeItem;

    public EcouteurSelectionCheckBox(CheckBox checkBox, TreeItem<String> treeItem) {
        this.checkBox = checkBox;
        this.treeItem = treeItem;
    }

    @Override
    public void invalidated(Observable observable) {
        if(checkBox.isSelected()) {
            if (currentSelectedCheckbox != null)
                currentSelectedCheckbox.setDisable(false);
            ARBRE_REPERTOIRES.setDisable(true);
            currentSelectedCheckbox = checkBox ;
            TâcheChargerFichier tacheChargerFichier = new TâcheChargerFichier(treeItem);
            ProgressIndicator progressIndicator = new ProgressIndicator() ;
            progressIndicator.progressProperty().bind(tacheChargerFichier.progressProperty());
            BORDER_PANE_DROIT.setCenter(progressIndicator);
            Thread thread = new Thread(tacheChargerFichier) ;
            thread.start();
        }
    }
}

class EcouteurSelectionTreeItem implements InvalidationListener {

    @Override
    public void invalidated(Observable observable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
