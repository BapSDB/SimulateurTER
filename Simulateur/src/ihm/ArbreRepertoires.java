
package ihm;

import static ihm.Vue.SIMULATEUR;
import static ihm.Vue.FICHIER;
import static ihm.Vue.RIGHT_BORDER_PANE;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.FileSystems;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import simulateur.Simulateur;
import traducteur.FabriqueTraducteur;
import static ihm.Vue.AFFICHAGE_CONSOLE;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.TextFieldTreeCell;
import static util.TimeStamp.getTimestampAffichageConsole;

public final class ArbreRepertoires extends TreeView<String> {
    
    private static TreeItem<String> rootItem ;
    private static CheckBox oldSelectedCheckbox, newSelectedCheckbox ;

    public ArbreRepertoires(String nomRepertoireRacine) {
        creerArbre(nomRepertoireRacine);
    }
    
    @FunctionalInterface
    private interface CreerArbre {
        public void creerArbre (CreerArbre ca, File fichier, TreeItem<String> parent);
    }
    
    public void creerArbre(String nomRepertoireRacine) {
        
        CreerArbre creerArbre = new CreerArbreImpl();
        
        rootItem = new TreeItem<>(nomRepertoireRacine);
        setShowRoot(false);
        setCellFactory(TextFieldTreeCell.forTreeView()) ;
        File fileInputDirectoryLocation = new File(nomRepertoireRacine);
        File fileList[] = fileInputDirectoryLocation.listFiles();
        for (File file : fileList) {
            creerArbre.creerArbre(creerArbre, file, rootItem);
        }
        setRoot(rootItem);
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
                //treeItem.addEventHandler(CheckBoxTreeItem.<String>checkBoxSelectionChangedEvent(), new SelectionHandlerImpl());
                treeItem.addEventHandler(TreeItem.<String>branchCollapsedEvent(), Event::consume);
                CheckBox checkBox = new CheckBox() ;
                treeItem.setGraphic(checkBox) ;
                checkBox.selectedProperty().bindBidirectional(checkBox.disableProperty());
                checkBox.selectedProperty().addListener((observable) -> {
                    ArbreRepertoires.this.setDisable(true) ;
                    newSelectedCheckbox = checkBox ;
                    TacheChargerFichier tacheChargerFichier = new TacheChargerFichier(treeItem);
                    ProgressIndicator progressIndicator = new ProgressIndicator() ;
                    progressIndicator.progressProperty().bind(tacheChargerFichier.progressProperty());
                    RIGHT_BORDER_PANE.setCenter(progressIndicator);
                    Thread thread = new Thread(tacheChargerFichier) ;
                    thread.start();
                });
            }
            parent.getChildren().add(treeItem);
        }

        private class SelectionHandlerImpl implements EventHandler<CheckBoxTreeItem.TreeModificationEvent<String>> {

            @Override
            public void handle(CheckBoxTreeItem.TreeModificationEvent<String> event) {
                //Déselectionner ds = new DéselectionnerImpl() ;
                //ds.déselectionner(ds, rootItem, event.getTreeItem());
            }
        }
    }
    
    @FunctionalInterface
    private interface ObtenirNomFichier {
        public String obtenirNomFichier(ObtenirNomFichier onf, TreeItem<String> treeItem, String nom) ;
    }
    
    private static String obtenirNomFichier(TreeItem<String> treeItem) {
        ObtenirNomFichier obtenirNomFichier = new ObtenirNomFichierImpl() ;
        return obtenirNomFichier.obtenirNomFichier(obtenirNomFichier, treeItem, "") ;
    }
    
    private static class ObtenirNomFichierImpl implements ObtenirNomFichier {

        @Override
        public String obtenirNomFichier(ObtenirNomFichier onf, TreeItem<String> treeItem, String nom) {
            if (treeItem.getParent() != null) {
                return onf.obtenirNomFichier(onf, treeItem.getParent(), nom).concat(FileSystems.getDefault().getSeparator() + treeItem.getValue());
            }
            return treeItem.getValue() ;
        }
    }
    
    @FunctionalInterface
    private interface Déselectionner {
        public void déselectionner(Déselectionner ds, TreeItem<String> treeItem, CheckBoxTreeItem<String> elt);
    }
    
    private static class DéselectionnerImpl implements Déselectionner {

        @Override
        public void déselectionner(Déselectionner ds, TreeItem<String> treeItem, CheckBoxTreeItem<String> elt) {
            if (treeItem.isLeaf()) {
                System.out.println(treeItem.getChildren());
                if (treeItem != elt)
                    elt.setSelected(false);
            }
            else
                for (TreeItem<String> fils : treeItem.getChildren())
                    déselectionner(ds, fils, elt);
        }
        
    }
    
    private class TacheChargerFichier extends Task<Void> {
        
        private final TreeItem<String> treeItem ;

        public TacheChargerFichier(TreeItem<String> treeItem) {
            this.treeItem = treeItem;
        }
        
        @Override
        protected Void call() throws Exception {

            SIMULATEUR = new Simulateur(FabriqueTraducteur.nouvelleFabrique(obtenirNomFichier(treeItem), new EcouteurAffichage()).creer()) ;
            FICHIER = new VueTableau(SIMULATEUR.getEntete(), SIMULATEUR.getContenu().toString()) ;
            
            Platform.runLater(() -> {
                RIGHT_BORDER_PANE.setCenter(FICHIER);
                ArbreRepertoires.this.setDisable(false);
                if (oldSelectedCheckbox != null)
                    oldSelectedCheckbox.setSelected(false);
                oldSelectedCheckbox = newSelectedCheckbox;
            });

            return null ;
        }

    }
    
    private static class EcouteurAffichage implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Platform.runLater(() -> {
                AFFICHAGE_CONSOLE.getChildren().add(new Text(getTimestampAffichageConsole() + evt.getNewValue().toString()));
            });
        }
    }
    
    /*private class FabriqueCellule implements Callback<TreeView<String>, TreeCell<String>> {

        @Override
        public TreeCell<String> call(TreeView<String> param) {
            return new CheckBoxTreeCell<String>() {
                
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!(getTreeItem() instanceof CheckBoxTreeItem))
                        setGraphic(null);
                }
            };
        }
    }*/
}
