package simulateur;

import traducteur.FabriqueTraducteur;
import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.SimulateurException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.Set;
import exceptions.one_event_by_line.* ;
import java.io.File;
import java.util.Arrays;
import java.util.Map.Entry;
import traducteur.Traducteur;
import util.StringUtil;
import util.TimeStamp;
import util.Util;

public final class Simulateur {
    
    private final FabriqueSimulateur fs ;
    public static final String RESSOURCES = "src/ressources/" ;
    public static final String OEBL = RESSOURCES + "oebl/" ;
    public static final String CSV = RESSOURCES + "csv/" ;
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "one-event-by-line"
    // Format attendu : timestamp ; objet ; value
    // Lexèmes correspondants : TIMESTAMP SEPARATEUR NOM_OBJET SEPARATEUR VALEUR
    // TIMESTAMP : HH:mm:ss
    // SEPARATEUR : ";" par défaut pouvant être entouré par des blancs
    // NOM_OBJET : une chaîne de caractères alphanumériques commençant par une lettre
    // VALEUR : une chaîne de caractères alphanumériques
    
    private static final String TIMESTAMP = TimeStamp.FORMAT_UNSIGNED_LONG_LONG ;
    private static final String BLANCS = "[^\\S\\n]*" ;
            static final String SEPARATEUR = BLANCS + ";" + BLANCS ;
    private static final String NOM_OBJET = "[A-Za-z]\\w*" ;
    private static final String VALEUR = "[\\w\\.]+" ;
    private static final Pattern PATTERN_ONE_EVENT_BY_LINE = Pattern.compile(TIMESTAMP+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR) ;
    
    public Simulateur(FabriqueSimulateur fs) {
	this.fs = fs ;
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
	if (!fs.existe && fs.estOEBL) {
	    System.out.println("lol");
	    Util.lireDonnees(PATTERN_ONE_EVENT_BY_LINE, fs.ajouterElement, new OneEventByLineTraiterFichierExceptions(OEBL+Util.obtenirNomFichier(fs.configurateur.getNomFichierOEBL())));
	}
    }
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV () throws OneEventByLineEcrireFormatCSVException {
	if (!new File(CSV+Util.obtenirNomFichier(fs.configurateur.getNomFichierCSV())).exists()) {
	    System.out.println("lol");
	    System.out.println(Arrays.toString(fs.padding));
	    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CSV+Util.obtenirNomFichier(fs.configurateur.getNomFichierCSV())))) {
		bufferedWriter.write(StringUtil.centrer("timestamp", fs.padding[0]) + Util.SEPARATEUR) ;
		Set<String> nomsObjets = fs.configurateur.getNomsObjets() ;
		int n = 0 ;
		for (String nomObjet : nomsObjets)
		    bufferedWriter.write(StringUtil.centrer(nomObjet, fs.padding[n+1]) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n")) ;
		for (Entry<String, String[]> entrySet : fs.tableau.entrySet()) {
		    n = 0 ;
		    bufferedWriter.write(StringUtil.centrer(entrySet.getKey(), fs.padding[0]) + Util.SEPARATEUR);
		    for (String valeur : entrySet.getValue())
			bufferedWriter.write(StringUtil.centrer(valeur != null ? valeur : "", fs.padding[n+1]) + (++n < entrySet.getValue().length ? Util.SEPARATEUR : "\n"));
		}
	    } catch (IOException ex) {
		ex.printStackTrace(System.err);
		throw new OneEventByLineEcrireFormatCSVException(CSV+Util.obtenirNomFichier(fs.configurateur.getNomFichierCSV())) ;
	    }
	}
    }
    
    private static void verifierArguments (String [] args) {
	if (args.length < 1) {
	    System.err.println("usage : java simulateur.Simulateur <nom_de_fichier_de_traces.(sw2|mqtt|oebl)");
	    System.exit(99);
	}
    }
    
    private static void traduireFormatOriginalVersFormatCSV (String nomFichierOriginal) {
	try {
	    Traducteur traducteur = FabriqueTraducteur.nouvelleFabrique(nomFichierOriginal).creer() ;
	    traducteur.appliquerTraduction();
	    Simulateur simulateur = traducteur.nouvelleFabriqueConfigurateur().creer().nouveauSimulateur().creer() ;
	    simulateur.lireFormatOneEventByLine();
	    simulateur.ecrireFormatCSV();
	    //Util.execCommande(new String[]{"cat",traducteur.getNomFichierOEBL()});
	    //Util.execCommande(new String[]{"cat", traducteur.getNomFichierCSV()});
	} catch (SimulateurException ex) {
	    ex.terminerExecutionSimulateur();
	}
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
	    verifierArguments(args);
	    traduireFormatOriginalVersFormatCSV(args[0]);
    }
    
}
