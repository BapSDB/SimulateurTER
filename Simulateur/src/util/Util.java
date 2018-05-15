package util;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TraiterFichierExceptions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import simulateur.Simulateur;
import traducteur.Traducteur.TraduireLigne;

public class Util {
    
    public static final String SEPARATEUR = " ; " ;
    
    @FunctionalInterface
    public static interface AjouterElement {
	public void ajouter (String ligne, String donnees, int numLigne) throws LireDonneesException ;
    }
    
    public static void lireDonnees (String nomFichierEntree, Pattern pattern, AjouterElement ajouterElement, TraiterFichierExceptions traiterFichierExceptions)
	    throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	String ligne ;
	try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(nomFichierEntree))) {
	    while ((ligne = lineNumberReader.readLine()) != null) {
		try(Scanner scannerLigne = new Scanner(ligne)) {
		    ajouterElement.ajouter(ligne, scannerLigne.findInLine(pattern), lineNumberReader.getLineNumber());
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw traiterFichierExceptions.getFichierIntrouvableException() ;
	} catch (IOException ex) {
	    throw traiterFichierExceptions.getEntreeSortieException() ;
	}
    }
    
    public static void traduireFormatOriginalVersFormatOEBL(String nomFichierOriginal, String nomFichierOEBL, Pattern pattern, TraduireLigne traduireLigne, TraiterFichierExceptions traiterFichierExceptions) throws FichierIntrouvableException, EntreeSortieException {
	String ligne ;
	try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(nomFichierOriginal)) ; BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierOEBL))) {
	    while ((ligne = lineNumberReader.readLine()) != null) {
		try(Scanner scannerLigne = new Scanner(ligne)) {
		    String ligneOEBL = traduireLigne.traduireLigne(ligne, scannerLigne.findInLine(pattern), lineNumberReader.getLineNumber()) ;
		    bufferedWriter.write(ligneOEBL != null ? (ligneOEBL+"\n") : "");
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw traiterFichierExceptions.getFichierIntrouvableException() ;
	} catch (IOException ex) {
	    throw traiterFichierExceptions.getEntreeSortieException() ;
	}
    }
    
    public static void execCommande (String [] cmd) {
	try {
	    Process p = Runtime.getRuntime().exec(cmd);
	    new Thread(() -> {
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())) ;
		    String line ;
		    while((line = br.readLine()) != null)
			System.out.println(line);
		} catch (IOException ex) {
		    Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }).start();
	    p.waitFor() ;
	} catch (IOException | InterruptedException ex) {
	    Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public static String[] separerCheminNomFichier (String nomFichier) {
	int i = nomFichier.lastIndexOf("/") ;
	return new String[]{nomFichier.substring(0, i+1), nomFichier.substring(i+1)} ;
    }
    
    public static String[] separerNomFichierExtension (String nomFichier) {
	int i = nomFichier.lastIndexOf(".") ;
	return new String[]{nomFichier.substring(0, i+1), nomFichier.substring(i+1)} ;
    }
    
    /*public static void main(String[] args) {
	System.out.println(java.util.Arrays.toString(separerNomFichierExtension(separerCheminNomFichier("././/MQTT a4h ___ 1440497600511.log")[1])));
    }*/
}
