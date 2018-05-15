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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;
import util.Util.AjouterElement;

public final class Simulateur {
    
    private static final int NB_EVENEMENTS = 2 << 16 ;
    private final Configurateur configurateur ;
    private final AjouterElement ajouterElement ;
    private final String nomFichierEntreeOEBL ;
    private String [] ligneDecoupee ;
    private List<Evenement> evenements ;
    private Map<String, List<String>> tableau ;
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "one-event-by-line"
    // Format attendu : timestamp ; objet ; value
    // Lexèmes correspondants : TIMESTAMP SEPARATEUR NOM_OBJET SEPARATEUR VALEUR
    // TIMESTAMP : HH:mm:ss
    // SEPARATEUR : ";" par défaut pouvant être entouré par des blancs
    // NOM_OBJET : une chaîne de caractères alphanumériques commençant par une lettre
    // VALEUR : une chaîne de caractères alphanumériques commençant par une lettre
    
    private static final String TIMESTAMP = "[0-2][0-3]:[0-5]\\d:[0-5]\\d" ;
    private static final String BLANCS = "[^\\S\\n]*" ;
    private static final String SEPARATEUR = BLANCS + ";" + BLANCS ;
    private static final String NOM_OBJET = "[A-Za-z]\\w*" ;
    private static final String VALEUR = "[A-Za-z]\\w*" ;
    private static final Pattern PATTERN_ONE_EVENT_BY_LINE = Pattern.compile(TIMESTAMP+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR) ;
    
    public Simulateur(Configurateur configurateur, String nomFichierEntreeOEBL) {
	
	this.configurateur = configurateur;
	this.nomFichierEntreeOEBL = nomFichierEntreeOEBL ;
        
	ajouterElement = (String ligne, String donnees, int numLigne) -> {
	    if (donnees != null) {
		ligneDecoupee = donnees.split(SEPARATEUR) ;
		if (configurateur.getNomsObjets().contains(ligneDecoupee[1])) {
                    try {
                        evenements.add(new Evenement(ligneDecoupee)) ;
                        tableau.get(ligneDecoupee[0]).add(evenements.get(evenements.size()-1).getValeur());
                    } catch (TimeStampParseException ex) {
                        Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		else
		    throw new OneEventByLineNomObjetIntrouvableException(ligneDecoupee[1], numLigne, nomFichierEntreeOEBL);
	    }
	    else {
		throw new OneEventByLineFormatException(ligne, numLigne, nomFichierEntreeOEBL);
	    }
	};
	
	ligneDecoupee = new String[3];
	evenements = new ArrayList<>(NB_EVENEMENTS) ;
        tableau = new LinkedHashMap<>(this.configurateur.getNomsObjets().size()) ;
        this.configurateur.getNomsObjets().forEach((nomObjet) -> {
            tableau.put(nomObjet, new LinkedList<>());
        });
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
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @param nomFichierSortie
     * * Le chemin (absolu ou relatif) <p> + nom du fichier où seront écrites les données
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV (String nomFichierSortie) throws OneEventByLineEcrireFormatCSVException {
	try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierSortie))) {
	    bufferedWriter.write("timestamp"+Evenement.SEPARATEUR) ;
	    Set<String> nomsObjets = configurateur.getNomsObjets() ;
	    int n = nomsObjets.size() ;
	    for (String nomObjet : nomsObjets)
		bufferedWriter.write(nomObjet + (--n > 0 ? Evenement.SEPARATEUR : "\n")) ;
	    for (Entry<String, List<String>> entrySet : tableau.entrySet()) {
                n = entrySet.getValue().size() ;
                bufferedWriter.write(entrySet.getKey()+Evenement.SEPARATEUR);
                for (String valeur : entrySet.getValue())
                    bufferedWriter.write(valeur + (--n > 0 ? Evenement.SEPARATEUR : "\n"));
            }
	} catch (IOException ex) {
	    throw new OneEventByLineEcrireFormatCSVException(nomFichierSortie) ;
	}
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
	try {
	    Simulateur simulateur = new Simulateur(new Configurateur("src/ressources/fichier_config.txt"), "test/one_event_by_line/ressources/OneEventByLineFormatCorrect.txt") ;
	    simulateur.lireFormatOneEventByLine();
	    simulateur.ecrireFormatCSV("test/one_event_by_line/ressources/fichier_tabulaire.csv");
	    Util.execCommande(new String[]{"cat","test/one_event_by_line/ressources/fichier_tabulaire.csv"});
	} catch (SimulateurException ex) {
	    ex.terminerExecutionSimulateur();
	}
    }
    
}
