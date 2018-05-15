
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import static util.Util.separerCheminNomFichier;
import static util.Util.separerNomFichierExtension;

public abstract class Traducteur {
    
    protected String nomFichierOriginal ;
    protected String nomFichierOEBL ;
    protected String [] evenements ;
    protected TraduireLigne traduireLigne ;

    public Traducteur(String nomFichierOriginal) {
	this.nomFichierOriginal = nomFichierOriginal;
	this.nomFichierOEBL = obtenirNomFichierOEBL();
    }

    public abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException ;
    
    private String obtenirNomFichierOEBL () {
	String [] cheminNomfichier = separerCheminNomFichier(nomFichierOriginal) ;
	String [] nomFichierExtension = separerNomFichierExtension(cheminNomfichier[1]) ;
	return cheminNomfichier[0] + nomFichierExtension[0] + "oebl" ;
    }
    
    @FunctionalInterface
    public interface TraduireLigne {
	public String traduireLigne(String ligne, String donnees, int numLigne);
    }

    public String getNomFichierOriginal() {
	return nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return nomFichierOEBL;
    }
    
}