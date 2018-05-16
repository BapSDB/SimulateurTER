
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
    
    /**
     * Traduit un fichier de traces spécifié dans un fichier au format "One-Event-By-Line"
     * @throws FichierIntrouvableException
     * si le fichier à traduire n'existe pas.
     * @throws EntreeSortieException
     * si une erreur d'entrée/sortie est apparue.
     * @throws TimeStampException
     * si un timestamp n'a pas pu être parsé ou est incohérent.
     */
    public abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, TimeStampException ;
    
    /**
     * Permet au Traducteur de spécifier la traduction d'une ligne d'un fichier
     */
    @FunctionalInterface
    public interface TraduireLigne {
	/**
	 * 
	 * @param ligne
	 * la ligne de données à traduire <p>
	 * @param donnees
	 * la ligne de données parsées <p>
	 * @param numLigne
	 * le n° de la ligne en cours de traduction <p>
	 * @param config
	 * permet d'écrire un nouveau nom d'objet dans le fichier de configuration <p>
	 * @return la ligne de données traduite
	 * @throws TimeStampException 
	 * si un timestamp n'a pas pu être parsé ou est incohérent.
	 */
	public String traduireLigne(String ligne, String donnees, int numLigne, BufferedWriter config) throws TimeStampException ;
    }

    public String getNomFichierOriginal() {
	return nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return nomFichierOEBL;
    }
    
}