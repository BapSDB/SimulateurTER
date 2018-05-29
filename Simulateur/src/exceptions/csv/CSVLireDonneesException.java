
package exceptions.csv;

import exceptions.SimulateurException;

public class CSVLireDonneesException extends SimulateurException {

    public CSVLireDonneesException(String nomFichierCSV) {
        super("Une erreur est apparue lors de la lecture des données du fichier CSV " + nomFichierCSV);
        codeErreur = 7 ;
    }
}
