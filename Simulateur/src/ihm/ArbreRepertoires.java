
package ihm;

import static ihm.Vue.CONSOLE;
import static ihm.Vue.SIMULATEUR;
import static ihm.Vue.FICHIER;
import static ihm.Vue.RIGHT_BORDER_PANE;
import java.io.File;
import java.nio.file.FileSystems;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import simulateur.Simulateur;
import traducteur.FabriqueTraducteur;

public final class ArbreRepertoires extends TreeView<String> {

    public ArbreRepertoires(String nomRepertoireRacine) {
        creerArbre(nomRepertoireRacine);
    }
    
    @FunctionalInterface
    private interface CreerArbre {
        public void creerArbre (CreerArbre ca, File fichier, TreeItem<String> parent);
    }
    
    public void creerArbre(String nomRepertoireRacine) {
        CreerArbre creerArbre = (ca, fichier, parent) -> {
            TreeItem<String> treeItem ;
            if (fichier.isDirectory()) {
                treeItem = new TreeItem<>(fichier.getName()) ;
                for (File f : fichier.listFiles())
                    ca.creerArbre(ca, f, treeItem);
            }
            else {
                treeItem = new CheckBoxTreeItem<>(fichier.getName()) ;
                ((CheckBoxTreeItem<String>)treeItem).selectedProperty().addListener((Observable observable) -> {
                    TacheChargerFichier tacheChargerFichier = new TacheChargerFichier(treeItem);
                    ProgressIndicator progressIndicator = new ProgressIndicator() ;
                    progressIndicator.progressProperty().bind(tacheChargerFichier.progressProperty());
                    RIGHT_BORDER_PANE.setCenter(progressIndicator);
                    Thread thread = new Thread(tacheChargerFichier) ;
                    thread.start();
                });
            }
            parent.getChildren().add(treeItem);
        };
        
        TreeItem<String> rootItem = new TreeItem<>(nomRepertoireRacine);
        setShowRoot(false);
        setCellFactory(new FabriqueCellule()) ;
        File fileInputDirectoryLocation = new File(nomRepertoireRacine);
        File fileList[] = fileInputDirectoryLocation.listFiles();
        for (File file : fileList) {
            creerArbre.creerArbre(creerArbre, file, rootItem);
        }
        setRoot(rootItem);
        
    }
    
    private static class TacheChargerFichier extends Task<Void> {
        
        private final TreeItem<String> treeItem ;

        public TacheChargerFichier(TreeItem<String> treeItem) {
            this.treeItem = treeItem;
        }
        
        @Override
        protected Void call() throws Exception {

            SIMULATEUR = new Simulateur(FabriqueTraducteur.nouvelleFabrique(obtenirNomFichier(treeItem)).creer()) ;
            FICHIER = new VueTableau(SIMULATEUR.getEntete(), SIMULATEUR.getContenu().toString()) ;

            Platform.runLater(() -> {
                RIGHT_BORDER_PANE.setCenter(FICHIER);
                CONSOLE.setContent(new Text(SIMULATEUR.getTraducteur().getConsole().toString()));
            });

            return null ;
        }
        
    }
    
    @FunctionalInterface
    private interface ObtenirNomFichier {
        public String obtenirNomFichier(ObtenirNomFichier onf, TreeItem<String> treeItem, String nom) ;
    }
    
    private static String obtenirNomFichier(TreeItem<String> treeItem) {
        ObtenirNomFichier obtenirNomFichier = (onf, tItem, nom) -> {
            if (tItem.getParent() != null) {
                return onf.obtenirNomFichier(onf, tItem.getParent(), nom).concat(FileSystems.getDefault().getSeparator() + tItem.getValue());
            }
            return tItem.getValue() ;
        } ;
        return obtenirNomFichier.obtenirNomFichier(obtenirNomFichier, treeItem, "") ;
    }

    private class FabriqueCellule implements Callback<TreeView<String>, TreeCell<String>> {

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
    }
    
}
