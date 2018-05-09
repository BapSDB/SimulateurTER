package simulateur;

import configurateur.Configurateur;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import util.Util;
import exceptions.FichierOneEventByLineIntrouvableException;
import exceptions.OneEventByLineFormatException;
import java.util.ArrayList;

public class Simulateur {
    
    private final Configurateur configurateur ;
    String [] ligneDecoupee ;
    List<Evenement> evenements ; 
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "one-event-by-line"
    // Format attendu : timestamp ; objet ; value
    // Lexèmes correspondants : TIMESTAMP SEPARATEUR NOM_OBJET SEPARATEUR VALEUR
    
    private static final String TIMESTAMP = "[0-2][0-3]:[0-5]\\d:[0-5]\\d" ;
    private static final String SEPARATEUR = "\\s*;\\s*" ;
    private static final String NOM_OBJET = "\\w+" ;
    private static final String VALEUR = "\\w+" ;
    private static final Pattern PATTERN_ONE_EVENT_BY_LINE = Pattern.compile(TIMESTAMP+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR) ;

    public Simulateur(Configurateur configurateur) {
	this.configurateur = configurateur;
	ligneDecoupee = new String[3];
	evenements = new ArrayList<>(128) ;
    }

    public Configurateur getConfigurateur() {
	return configurateur;
    }
    
    /**
     * Lit un fichier "one-event-by-line" et stocke ses données.<p>
     * Format attendu par ligne : timestamp ; objet ; value
     * @param nomFichierEntree
     * Le chemin (absolu ou relatif) <p> + nom du fichier one-event-by-line à lire
     * @throws FichierOneEventByLineIntrouvableException
     * si le fichier n'existe pas.<p>
     * @throws OneEventByLineFormatException
     * si une ligne du fichier ne correspond pas au format attendu.<p>
     * @since V0
     */
    private void lireFormatOneEventByLine (String nomFichierEntree) throws FichierOneEventByLineIntrouvableException, OneEventByLineFormatException {
	Scanner scanner ;
	try {
	    scanner = new Scanner(new File(nomFichierEntree)) ;
	    int numLigne = 0 ;
	    while (scanner.hasNext()) {
		scanner.skip("\\s*") ;
		String ligne = scanner.nextLine() ;
		String donnees = new Scanner(ligne).findInLine(PATTERN_ONE_EVENT_BY_LINE) ;
		numLigne++ ;
		if (donnees != null) {
		    ligneDecoupee = donnees.split(SEPARATEUR) ;
		    System.out.println(java.util.Arrays.toString(ligneDecoupee));
		}
		else {
		    scanner.close();
		    throw new OneEventByLineFormatException(numLigne, ligne);
		}
	    }
	    //System.out.println(nomsObjets);
	    scanner.close();
	} catch (FileNotFoundException ex) {
	    throw new FichierOneEventByLineIntrouvableException(nomFichierEntree);
	}
    }
    
    private void ecrireFormatCSV (String nomFichierSortie) {
	BufferedWriter bufferedWriter ;
	try {
	    bufferedWriter = new BufferedWriter(new FileWriter(nomFichierSortie));
	    bufferedWriter.write("timestamp"+Evenement.SEPARATEUR) ;
	    List<String> nomsObjets = configurateur.getNomsObjets() ;
	    int i = 0 ;
	    for (; i < nomsObjets.size()-1; i++) {
		bufferedWriter.write(nomsObjets.get(i) + Evenement.SEPARATEUR) ;
	    }
	    bufferedWriter.write(nomsObjets.get(i) + "\n");
	    bufferedWriter.close();
	} catch (IOException ex) {
	    System.err.println("Une erreur est apparue lors de l'écriture des traces au format CSV dans le fichier " + nomFichierSortie + ".") ;
	    System.exit(3);
	}
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
	try {
	    Simulateur simulateur = new Simulateur(new Configurateur()) ;
	    //simulateur.getConfigurateur().lireObjets("ressources/fichier_config.txt");
	    //simulateur.getConfigurateur().lireObjets("ressources/fichier_tabulaire_OneEventByLineFormatException.txt");
	    simulateur.lireFormatOneEventByLine("ressources/fichier_tabulaire_OneEventByLineFormatException.txt");
	    simulateur.ecrireFormatCSV("ressources/fichier_tabulaire.csv");
	    Util.execCommande(new String[]{"cat","ressources/fichier_tabulaire.csv"});
	} catch (FichierOneEventByLineIntrouvableException | OneEventByLineFormatException ex) {
	    ex.terminerExecutionSimulateur();
	}
    }
    
}
