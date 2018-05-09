package simulateur;

import configurateur.Configurateur;
import exceptions.one_event_by_line.OneEventByLineEcrireFormatCSVException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import exceptions.one_event_by_line.OneEventByLineFichierIntrouvableException;
import exceptions.one_event_by_line.OneEventByLineFormatException;
import java.util.ArrayList;
import java.util.Collection;
import util.Util;

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
    private static final Pattern SKIP = Pattern.compile("[^\\S\\n]*\\n") ;

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
     * @throws OneEventByLineFichierIntrouvableException
     * si le fichier n'existe pas.<p>
     * @throws OneEventByLineFormatException
     * si une ligne du fichier ne correspond pas au format attendu.<p>
     * @since V0
     * @see ecrireFormatCSV
     */
    
    public void lireFormatOneEventByLine (String nomFichierEntree) throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException {
	try (Scanner scanner = new Scanner(new File(nomFichierEntree))) {
	    scanner.useDelimiter("");
	    int numLigne = 0 ;
	    while (scanner.hasNext()) {
		while (scanner.hasNext(SKIP)) {
		    scanner.skip(SKIP);
		    numLigne++ ;
		}
		String ligne = scanner.nextLine() ;
		try(Scanner scannerLigne = new Scanner(ligne)) {
		    String donnees = scannerLigne.findInLine(PATTERN_ONE_EVENT_BY_LINE) ;
		    numLigne++ ;
		    if (donnees != null) {
			ligneDecoupee = donnees.split(SEPARATEUR) ;
			evenements.add(new Evenement(ligneDecoupee)) ;
		    }
		    else {
			throw new OneEventByLineFormatException(numLigne, ligne, nomFichierEntree);
		    }
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw new OneEventByLineFichierIntrouvableException(ex, nomFichierEntree);
	}
    }
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @param nomFichierSortie
     * * Le chemin (absolu ou relatif) <p> + nom du fichier où seront écrites les données
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur d'entrée/sortie est apparue lors de l'écriture
     */
    public void ecrireFormatCSV (String nomFichierSortie) throws OneEventByLineEcrireFormatCSVException {
	try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierSortie))) {
	    bufferedWriter.write("timestamp"+Evenement.SEPARATEUR) ;
	    Collection<String> nomsObjets = configurateur.getNomsObjets() ;
	    int n = nomsObjets.size() ;
	    for (String nomObjet : nomsObjets)
		bufferedWriter.write(nomObjet + (--n > 0 ? Evenement.SEPARATEUR : "\n")) ;
	    for (Evenement evenement : evenements)
		bufferedWriter.write(evenement.toString()+"\n");
	} catch (IOException ex) {
	    throw new OneEventByLineEcrireFormatCSVException(ex, nomFichierSortie) ;
	}
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
	try {
	    Simulateur simulateur = new Simulateur(new Configurateur("ressources/fichier_config.txt")) ;
	    simulateur.ecrireFormatCSV("test/one_event_by_line/ressources/fichier_tabulaire.csv");
	    Util.execCommande(new String[]{"cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
	} catch (OneEventByLineEcrireFormatCSVException ex) {
	    ex.terminerExecutionSimulateur();
	}
    }
    
}
