
package exceptions.traducteur;

import exceptions.LireDonneesException;

public class TraducteurNomObjetNonUniqueException extends LireDonneesException {

    public TraducteurNomObjetNonUniqueException(String nomObjet, int numLigne, String nomFichierOriginal) {
        super("L'objet " + "\"" + nomObjet + "\" à la ligne n°" + numLigne + " du fichier " + nomFichierOriginal + " a déjà été répertorié");
        codeErreur = 15 ;
    }
    
}
