
package traducteur;

import configurateur.Configurateur;
import exceptions.SimulateurException;
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
import static simulateur.Formateur.CSV;
import static simulateur.Formateur.OEBL;
import util.Util;

public abstract class Traducteur {
    
    protected final FabriqueTraducteur ft ;
    public static final AffichageBean AFFICHAGE_BEAN = new AffichageBean() ;

    public Traducteur(FabriqueTraducteur ft) throws SimulateurException  {
	this.ft = ft ;
        appliquerTraduction() ;
    }
    
    /**
     * 
     * @throws SimulateurException
     */
    protected void appliquerTraduction () throws SimulateurException {
	String RacineOEBL = Util.obtenirNomFichier(ft.nomFichierOEBL) ;
	String RacineConfig = Util.obtenirNomFichier(ft.nomFichierConfig) ;
        String RacineCSV = Util.obtenirNomFichier(ft.nomFichierCSV) ;
        boolean existe ;
	ft.tableauCSV.setPaddingTimeStamp(Math.max("timestamp".length(), 0)) ;
        if(existe = !new File(OEBL+RacineOEBL).exists() || !new File(OEBL+RacineConfig).exists()) {
            AFFICHAGE_BEAN.setAffichage("Le fichier " + ft.nomFichierOriginal + " est en cours de traduction...\n") ;
	    traduireFormatOriginalVersFormatOEBL();
            AFFICHAGE_BEAN.setAffichage("Traduction terminée --> création des fichiers " + OEBL + RacineOEBL + " et " + OEBL + RacineConfig + "\n") ;
	}
        
        AFFICHAGE_BEAN.setAffichage("Le fichier " + ft.nomFichierOriginal + " existe déjà au format \"One-Event-By-Line\" --> \u00c9tape de traduction ignorée.\n") ;
        
        if (!existe && !new File(CSV+RacineCSV).exists()) {
            AFFICHAGE_BEAN.setAffichage("Le fichier " + getNomFichierOEBL() + " existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.\n") ;
            AFFICHAGE_BEAN.setAffichage("Chargement du fichier " + getNomFichierCSV() + "...\n") ;
            Configurateur.lireFormatOEBL(this);
            AFFICHAGE_BEAN.setAffichage("Chargement terminé.\n");
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
    protected void traduireFormatOriginalVersFormatOEBL() throws SimulateurException {
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
    
    public abstract Pattern getPattern () ;
    
    public abstract String getSeparateur();

}