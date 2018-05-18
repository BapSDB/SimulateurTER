
package exceptions;

public abstract class EntreeSortieException extends SimulateurException {
    
    public EntreeSortieException(String message, String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig) {
	super(message);
    }
}
