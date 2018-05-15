
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import static util.Util.obtenirNomFichier;

public abstract class Traducteur {
    
    protected Map<String,Integer> nomsObjets = new LinkedHashMap<>();
    protected String nomFichierOriginal ;
    protected String nomFichierOEBL ;
    protected String nomFichierConfig ;
    protected TraduireLigne traduireLigne ;

    public Traducteur(String nomFichierOriginal) {
	this.nomFichierOriginal = nomFichierOriginal;
	this.nomFichierOEBL = obtenirNomFichier(nomFichierOriginal, "oebl");
	this.nomFichierConfig = obtenirNomFichier(nomFichierOriginal, "config");
    }

    public abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException ;
    
    @FunctionalInterface
    public interface TraduireLigne {
	public String traduireLigne(String ligne, String donnees, int numLigne, BufferedWriter config) throws TimeStampException, IOException, LireDonneesException ;
    }

    public String getNomFichierOriginal() {
	return nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return nomFichierOEBL;
    }
    
}