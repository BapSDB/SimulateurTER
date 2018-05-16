
package exceptions;

public abstract class EntreeSortieException extends SimulateurException {
    
    private final String nomFichierOriginal ;
    private final String nomFichierOEBL ;
    private final String nomFichierConfig ;

    public EntreeSortieException(String message, String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig) {
	super(message);
	this.nomFichierOriginal = nomFichierOriginal;
	this.nomFichierOEBL = nomFichierOEBL;
	this.nomFichierConfig = nomFichierConfig;
    }
    
    public String getNomFichierOriginal() {
	return nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return nomFichierOEBL;
    }

    public String getNomFichierConfig() {
	return nomFichierConfig;
    }
    
}
