package simulateur;

import configurateur.Configurateur;
import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.SimulateurException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Set;
import exceptions.one_event_by_line.* ;
import util.Util;
import util.Util.AjouterElement;

public class Simulateur {
    
    private final Configurateur configurateur ;
    private final AjouterElement ajouterElement ;
    private final String nomFichierEntreeOEBL ;
    private String [] ligneDecoupee ;
    private List<Evenement> evenements ;
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "one-event-by-line"
    // Format attendu : timestamp ; objet ; value
    // Lexèmes correspondants : TIMESTAMP SEPARATEUR NOM_OBJET SEPARATEUR VALEUR
    
    private static final String TIMESTAMP = "[0-2][0-3]:[0-5]\\d:[0-5]\\d" ;
    private static final String SEPARATEUR = "\\s*;\\s*" ;
    private static final String NOM_OBJET = "\\w+" ;
    private static final String VALEUR = "\\w+" ;
    private static final Pattern PATTERN_ONE_EVENT_BY_LINE = Pattern.compile(TIMESTAMP+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR) ;
    
    public Simulateur(String nomFichierEntreeOEBL, Configurateur configurateur) {
	
	this.configurateur = configurateur;
	this.nomFichierEntreeOEBL = nomFichierEntreeOEBL;
	
	ajouterElement = (String donnees, int numLigne) -> {
	    if (donnees != null) {
		ligneDecoupee = donnees.split(SEPARATEUR) ;
		if (configurateur.getNomsObjets().contains(ligneDecoupee[1]))
		    evenements.add(new Evenement(ligneDecoupee)) ;
		else
		    throw new OneEventByLineNomObjetIntrouvableException(ligneDecoupee[1], numLigne, nomFichierEntreeOEBL);
	    }
	    else {
		throw new OneEventByLineFormatException(donnees, numLigne, nomFichierEntreeOEBL);
	    }
	};
	
	ligneDecoupee = new String[3];
	evenements = new ArrayList<>(128) ;
    }
    
    public Configurateur getConfigurateur() {
	return configurateur;
    }
    
    /**
     * Lit un fichier "one-event-by-line" et stocke ses données.<p>
     * Format attendu par ligne : timestamp ; objet ; value
     * @throws OneEventByLineFichierIntrouvableException
     * si le fichier n'existe pas.<p>
     * @throws OneEventByLineFormatException
     * si une ligne du fichier ne correspond pas au format attendu.<p>
     * @throws OneEventByLineNomObjetIntrouvableException
     * si l'objet lue sur la ligne n'a pas été répertorié par le configurateur
     * @throws OneEventByLineLireDonneesException
     * si une erreur est apparue lors de la lecture du fichier OEBL 
     * @since V0
     * @see ecrireFormatCSV
     */
    
    public void lireFormatOneEventByLine () throws FichierIntrouvableException, LireDonneesException, EntreeSortieException {
	Util.lireDonnees(nomFichierEntreeOEBL, PATTERN_ONE_EVENT_BY_LINE, ajouterElement, new OneEventByLineTraiterFichierExceptions(nomFichierEntreeOEBL));
    }
    
    /*public void lireFormatOneEventByLine (String nomFichierEntree) throws OneEventByLineFichierIntrouvableException, OneEventByLineFormatException, OneEventByLineNomObjetIntrouvableException {
	Set<String> nomsObjets = configurateur.getNomsObjets() ;
	try (Scanner scanner = new Scanner(new File(nomFichierEntree))) {
	    scanner.useDelimiter("");
	    int numLigne = 0 ;
	    while (scanner.hasNext()) {
		while (scanner.hasNext(SKIP)) {
		    scanner.skip(SKIP);
		    numLigne++ ;
		}
		if (scanner.hasNext()) {
		    String ligne = scanner.nextLine() ;
		    try(Scanner scannerLigne = new Scanner(ligne)) {
			String donnees = scannerLigne.findInLine(PATTERN_ONE_EVENT_BY_LINE) ;
			numLigne++ ;
			if (donnees != null) {
			    ligneDecoupee = donnees.split(SEPARATEUR) ;
			    
			    if (nomsObjets.contains(ligneDecoupee[1]))
				evenements.add(new Evenement(ligneDecoupee)) ;
			    else
				throw new OneEventByLineNomObjetIntrouvableException(ligneDecoupee[1], numLigne, nomFichierEntree);
			}
			else {
			    throw new OneEventByLineFormatException(numLigne, ligne, nomFichierEntree);
			}
		    }
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw new OneEventByLineFichierIntrouvableException(ex, nomFichierEntree);
	}
    }*/
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @param nomFichierSortie
     * * Le chemin (absolu ou relatif) <p> + nom du fichier où seront écrites les données
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur d'entrée/sortie est apparue lors de l'écriture
     * @since V0
     * @see lireFormatOneEventByLine
     */
    public void ecrireFormatCSV (String nomFichierSortie) throws OneEventByLineEcrireFormatCSVException {
	try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierSortie))) {
	    bufferedWriter.write("timestamp"+Evenement.SEPARATEUR) ;
	    Set<String> nomsObjets = configurateur.getNomsObjets() ;
	    int n = nomsObjets.size() ;
	    for (String nomObjet : nomsObjets)
		bufferedWriter.write(nomObjet + (--n > 0 ? Evenement.SEPARATEUR : "\n")) ;
	    for (Evenement evenement : evenements)
		bufferedWriter.write(evenement.toString()+"\n");
	} catch (IOException ex) {
	    throw new OneEventByLineEcrireFormatCSVException(nomFichierSortie) ;
	}
    }

  /**
   * @param args the command line arguments
   */

  public static void main(String[] args) {
    try {
        Simulateur simulateur = new Simulateur("test/one_event_by_line/ressources/OneEventByLineFormatCorrect.txt", new Configurateur("ressources/fichier_config.txt")) ;
        simulateur.lireFormatOneEventByLine();
        simulateur.ecrireFormatCSV("test/one_event_by_line/ressources/fichier_tabulaire.csv");
        Util.execCommande(new String[]{"cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
    } catch (SimulateurException ex) {
        ex.terminerExecutionSimulateur();
    }
    
}
