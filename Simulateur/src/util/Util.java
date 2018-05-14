package util;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TraiterFichierExceptions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import simulateur.Simulateur;

public class Util {
    
    @FunctionalInterface
    public static interface AjouterElement {
	public void ajouter (String donnees, int numLigne) throws LireDonneesException ;
    }
    
    public static void lireDonnees (String nomFichierEntree, Pattern pattern, AjouterElement ajouterElement, TraiterFichierExceptions traiterFichierExceptions)
	    throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	String ligne ;
	try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(nomFichierEntree))) {
	    while ((ligne = lineNumberReader.readLine()) != null) {
		try(Scanner scannerLigne = new Scanner(ligne)) {
		    String donnees = scannerLigne.findInLine(pattern) ;
		    ajouterElement.ajouter(donnees, lineNumberReader.getLineNumber());
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
}
