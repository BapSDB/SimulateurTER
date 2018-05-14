
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
    
}
