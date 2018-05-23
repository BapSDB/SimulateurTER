
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
import static simulateur.Simulateur.CSV;
import static simulateur.Simulateur.OEBL;
import util.Util;

public abstract class Traducteur {
    
    protected final FabriqueTraducteur ft ;

    public Traducteur(FabriqueTraducteur ft) throws SimulateurException  {
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
    protected void appliquerTraduction () throws SimulateurException {
	String RacineOEBL = Util.obtenirNomFichier(ft.nomFichierOEBL) ;
	String RacineConfig = Util.obtenirNomFichier(ft.nomFichierConfig) ;
        String RacineCSV = Util.obtenirNomFichier(ft.nomFichierCSV) ;
        boolean existe ;
	if(existe = !new File(OEBL+RacineOEBL).exists() || !new File(OEBL+RacineConfig).exists()) {
            ft.console.append("Le fichier ").append(ft.nomFichierOriginal).append(" est en cours de traduction...\n") ;
	    traduireFormatOriginalVersFormatOEBL();
            ft.console.append("Traduction terminée --> création des fichiers ").append(OEBL).append(RacineOEBL).append(" et ").append(OEBL).append(RacineConfig).append("\n");
	}
        
        ft.console.append("Le fichier ").append(ft.nomFichierOriginal).append(" existe déjà au format \"One-Event-By-Line\" --> \u00c9tape de traduction ignorée.\n");
        
        if (!existe && !new File(CSV+RacineCSV).exists()) {
            System.out.println("LOOOOOOOL");
            Configurateur.lireFormatOEBL(this);
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
    
    public StringBuilder getConsole() {
	return ft.console ;
    }
    
    public abstract Pattern getPattern () ;
    
    public abstract String getSeparateur();

}