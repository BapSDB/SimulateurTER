
package exceptions.traducteur;

import exceptions.SimulateurException;

public class TraducteurNomObjetNonUniqueException extends SimulateurException {

    public TraducteurNomObjetNonUniqueException(String nomObjet, int numLigne, String nomFichierOriginal) {
        super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierOriginal + " a déjà été répertorié");
        codeErreur = 4 ;
    }
    
}
