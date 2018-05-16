
package exceptions;

public abstract class TraiterFichierExceptions {
    
    private final FichierIntrouvableException fichierIntrouvableException ;
    private final EntreeSortieException entreeSortieException ;

    public TraiterFichierExceptions(FichierIntrouvableException fichierIntrouvableException, EntreeSortieException entreeSortieException) {
	this.fichierIntrouvableException = fichierIntrouvableException;
	this.entreeSortieException = entreeSortieException;
    }
    
    public FichierIntrouvableException getFichierIntrouvableException() {
	return fichierIntrouvableException;
    }

    public EntreeSortieException getEntreeSortieException() {
	return entreeSortieException;
    }
    
    public String getNomFichierLu() {
	return fichierIntrouvableException.getNomFichier() ;
    }
    
    public String getNomFichierOriginal() {
	return entreeSortieException.getNomFichierOriginal() ;
    }

    public String getNomFichierOEBL() {
	return entreeSortieException.getNomFichierOEBL() ;
    }

    public String getNomFichierConfig() {
	return entreeSortieException.getNomFichierConfig() ;
    }
    
}
