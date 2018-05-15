
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.traducteur.TraducteurTraiterFichierExceptions;
import java.util.regex.Pattern;
import util.TimeStamp;
import util.Util;

public class TraducteurSwitch2 extends Traducteur {
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "Switch2"
    // Format attendu : timestamp objet ; value
    // Lexèmes correspondants : TIMESTAMP ID NOM_OBJET SEPARATEUR VALEUR
    // TIMESTAMP : aaaa-MM-jj_HH:mm:ss
    // SEPARATEUR : blanc(s)
    // NOM_OBJET : power
    // VALEUR : une chaîne de caractères numérique
    // UNITE : en Watts
    
    private static final String TIMESTAMP = TimeStamp.FORMAT_DATE_HEURE ;
    private static final String ID = "ZWave_SWITCH_BINARY_2" ;
    private static final String BLANCS = "[^\\S\\n]+" ;
    private static final String SEPARATEUR = BLANCS ;
    private static final String NOM_OBJET = "power:" ;
    private static final String VALEUR = "\\d+" ;
    private static final String UNITE = "W" ;
    private static final Pattern PATTERN_SWITCH2 = Pattern.compile(TIMESTAMP+SEPARATEUR+ID+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR+SEPARATEUR+UNITE) ;

    public TraducteurSwitch2(String nomFichierOriginal) {
	super(nomFichierOriginal) ;
	evenements = new String[5];
	traduireLigne = (String ligne, String donnees, int numLigne) -> {
	    if (donnees != null) {
		evenements = donnees.split(SEPARATEUR) ;
		return evenements[0] + Util.SEPARATEUR + evenements[2].substring(0, evenements[2].length()-1) + Util.SEPARATEUR + evenements[3] ;
	    }
	    return null ;
	} ;
    }

    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException {
	Util.traduireFormatOriginalVersFormatOEBL(nomFichierOriginal, nomFichierOEBL, PATTERN_SWITCH2, traduireLigne, new TraducteurTraiterFichierExceptions(nomFichierOriginal, nomFichierOEBL));
    }
    
}
