
package exceptions;

public abstract class FichierIntrouvableException extends SimulateurException {
    
    public FichierIntrouvableException(String message, String nomFichier) {
	super(message);
    }
    
}
