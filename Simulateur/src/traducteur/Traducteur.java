
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import exceptions.traducteur.TraducteurFichierIntrouvableException;
import exceptions.traducteur.TraducteurTraduireFichierOriginalVersFichierOEBLException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.regex.Pattern;
import static simulateur.Simulateur.OEBL;
import util.Util;

public abstract class Traducteur {
    
    protected final FabriqueTraducteur ft ;
    private boolean existe ;

    public Traducteur(FabriqueTraducteur ft) throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	this.ft = ft ;
        ft.tableauCSV.setPaddingTimeStamp(Math.max("timestamp".length(), 0)) ;
        appliquerTraduction() ;
    }
    
    /**
     * 
     * @throws FichierIntrouvableException
     * @throws EntreeSortieException
     * @throws LireDonneesException
     * @throws TimeStampException 
     */
    protected void appliquerTraduction () throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	String RacineOEBL = Util.obtenirNomFichier(ft.nomFichierOEBL) ;
	String RacineConfig = Util.obtenirNomFichier(ft.nomFichierConfig) ;
	if(existe = !new File(OEBL+RacineOEBL).exists() || !new File(OEBL+RacineConfig).exists()) {
	    System.out.println("Le fichier " + ft.nomFichierOriginal + " est en cours de traduction...") ;
            ft.contenu.append("Le fichier ").append(ft.nomFichierOriginal).append(" est en cours de traduction...\n") ;
	    traduireFormatOriginalVersFormatOEBL();
	    System.out.println("Traduction terminée --> création des fichiers " + OEBL+RacineOEBL + " et " + OEBL+RacineConfig) ;
            ft.contenu.append("Traduction terminée --> création des fichiers ").append(OEBL).append(RacineOEBL).append(" et ").append(OEBL).append(RacineConfig).append("\n");
	}
        else {
            System.out.println("Le fichier " + ft.nomFichierOriginal + " existe déjà au format \"One-Event-By-Line\" --> \u00c9tape de traduction ignorée.");
            ft.contenu.append("Le fichier ").append(ft.nomFichierOriginal).append(" existe déjà au format \"One-Event-By-Line\" --> \u00c9tape de traduction ignorée.\n");
        }
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
    protected void traduireFormatOriginalVersFormatOEBL() 
	    throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	String ligne ;
	try (LineNumberReader  original = new LineNumberReader(new FileReader(ft.nomFichierOriginal)) ;
		BufferedWriter oebl = new BufferedWriter(new FileWriter(ft.nomFichierOEBL)) ;
		BufferedWriter config = new BufferedWriter(new FileWriter(ft.nomFichierConfig))) {
	    while ((ligne = original.readLine()) != null) {
		try(Scanner scannerLigne = new Scanner(ligne)) {
		    String ligneOEBL = ft.traduireLigne.traduireLigne(ligne, scannerLigne.findInLine(getPattern()), original.getLineNumber(), config) ;
		    oebl.write(ligneOEBL != null ? (ligneOEBL+"\n") : "");
		}
	    }
	} catch (FileNotFoundException ex) {
            System.out.println(ex);
	    throw new TraducteurFichierIntrouvableException(ft.nomFichierOriginal) ;
	} catch (IOException ex) {
	    throw new TraducteurTraduireFichierOriginalVersFichierOEBLException(ft.nomFichierOriginal, ft.nomFichierOEBL, ft.nomFichierConfig) ;
	}
    }
    
    public abstract boolean estOEBL () ;
    
    
    public TableauCSV getTableauCSV() {
        return ft.tableauCSV ;
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
    
    public StringBuilder getContenu() {
	return ft.contenu ;
    }
    
    public boolean Existe() {
	return existe;
    }
    
    public abstract Pattern getPattern () ;
    
    public abstract String getSeparateur();
    
}