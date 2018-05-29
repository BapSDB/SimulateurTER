
package exceptions.csv;

import exceptions.SimulateurException;

public class CSVFichierIntrouvableException extends SimulateurException {

    public CSVFichierIntrouvableException(String nomFichierCSV) {
        super("\"Le fichier au format CSV \"" + nomFichierCSV + "\" n'existe pas\"");
        codeErreur = 6 ;
    }
    
}
