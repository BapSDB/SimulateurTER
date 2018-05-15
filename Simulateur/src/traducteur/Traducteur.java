
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.TimeStampException;
import java.io.BufferedWriter;
import static util.Util.obtenirNomFichier;

public abstract class Traducteur {
    
    protected String nomFichierOriginal ;
    protected String nomFichierOEBL ;
    protected String nomFichierConfig ;
    protected TraduireLigne traduireLigne ;

    public Traducteur(String nomFichierOriginal) {
	this.nomFichierOriginal = nomFichierOriginal;
	this.nomFichierOEBL = obtenirNomFichier(nomFichierOriginal, "oebl");
	this.nomFichierConfig = obtenirNomFichier(nomFichierOriginal, "config");
    }

    public abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, TimeStampException ;
    
    @FunctionalInterface
    public interface TraduireLigne {
	public String traduireLigne(String ligne, String donnees, int numLigne, BufferedWriter config) throws TimeStampException ;
    }

    public String getNomFichierOriginal() {
	return nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return nomFichierOEBL;
    }
    
}