
package ihm.arbre_repertoires;

import java.nio.file.FileSystems;
import javafx.scene.control.TreeItem;

@FunctionalInterface
interface ObtenirNomFichier {
    
    public String obtenirNomFichier(ObtenirNomFichier onf, TreeItem<String> treeItem, String nom) ;
    
    static String obtenirNomFichier(TreeItem<String> treeItem) {
        ObtenirNomFichier obtenirNomFichier = new ObtenirNomFichierImpl() ;
        return obtenirNomFichier.obtenirNomFichier(obtenirNomFichier, treeItem, "") ;
    }
    
    static class ObtenirNomFichierImpl implements ObtenirNomFichier {

        @Override
        public String obtenirNomFichier(ObtenirNomFichier onf, TreeItem<String> treeItem, String nom) {
            if (treeItem.getParent() != null) {
                return onf.obtenirNomFichier(onf, treeItem.getParent(), nom).concat(FileSystems.getDefault().getSeparator() + treeItem.getValue());
            }
            return treeItem.getValue() ;
        }
    }
    
}
