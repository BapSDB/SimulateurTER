
package exceptions;

public abstract class FichierIntrouvableException extends SimulateurException {
    
    private final String nomFichier ;

    public FichierIntrouvableException(String message, String nomFichier) {
	super(message);
	this.nomFichier = nomFichier ;
    }

    public String getNomFichier() {
	return nomFichier;
    }
    
}
