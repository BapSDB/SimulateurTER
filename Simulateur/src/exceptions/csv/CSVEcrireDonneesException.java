
package exceptions.csv;

import exceptions.SimulateurException;

public class CSVEcrireDonneesException extends SimulateurException {
    
    public CSVEcrireDonneesException(String nomFichierCSV) {
	super("Une erreur est apparue lors de l'écriture des données dans le fichier CSV " + nomFichierCSV);
	codeErreur = 14 ;
    }
    
}
