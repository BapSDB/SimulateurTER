
package traducteur;

import configurateur.FabriqueConfigurateur;
import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import traducteur.FabriqueTraducteur.TraduireLigne;
import util.Util;
import static simulateur.Simulateur.OEBL;

public abstract class Traducteur {
    
    protected final FabriqueTraducteur ft ;
    private boolean existe ;
    private boolean estOEBL ;

    public Traducteur(FabriqueTraducteur ft) {
	this.ft = ft ;
    }
    
    /**
     * 
     * @throws FichierIntrouvableException
     * @throws EntreeSortieException
     * @throws LireDonneesException
     * @throws TimeStampException 
     */
    public void appliquerTraduction () throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	String RacineOEBL = Util.obtenirNomFichier(ft.nomFichierOEBL) ;
	String RacineConfig = Util.obtenirNomFichier(ft.nomFichierConfig) ;
	if(existe = !new File(OEBL+RacineOEBL).exists() || !new File(OEBL+RacineConfig).exists()) {
	    System.out.println("Le fichier " + ft.nomFichierOriginal + "est en cours de traduction...");
	    traduireFormatOriginalVersFormatOEBL();
	    System.out.println("Traduction terminée --> création des fichiers " + OEBL+RacineOEBL + " et " + OEBL+RacineConfig + ".") ;
	}
	System.out.println("Le fichier " + ft.nomFichierOriginal + " existe déjà au format \"One-Event-By-Line\" --> \u00c9tape de traduction ignorée.");
    }
    
    /**
     * Traduit un fichier de traces spécifié dans un fichier au format "One-Event-By-Line"
     * @throws FichierIntrouvableException
     * si le fichier à traduire n'existe pas.
     * @throws EntreeSortieException
     * si une erreur d'entrée/sortie est apparue.
     * @throws LireDonneesException
     * @throws TimeStampException
     * si un timestamp n'a pas pu être parsé ou est incohérent.
     */
    protected abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException ;
    
    public abstract boolean estOEBL () ;
    
    public Map<String, Integer> getNomsObjets() {
	return ft.nomsObjets;
    }
    
    public String getNomFichierOriginal() {
	return ft.nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return ft.nomFichierOEBL;
    }

    public String getNomFichierConfig() {
	return ft.nomFichierConfig;
    }
    
    public String getNomFichierCSV() {
	return ft.nomFichierCSV;
    }

    public boolean Existe() {
	return existe;
    }
    
    protected TraduireLigne getTraduireLigne () {
	return ft.traduireLigne ;
    }
    
    public FabriqueConfigurateur nouvelleFabriqueConfigurateur () {
	return new FabriqueConfigurateur(this) ;
    }
    
}