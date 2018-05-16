
package traducteur;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.TimeStampException;
import exceptions.fichier_config.ConfigEcrireObjetsException;
import exceptions.traducteur.TraducteurTraiterFichierExceptions;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import util.TimeStamp;
import util.Util;

public class TraducteurSwitch2 extends Traducteur {
    
    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "Switch2"
    // <nature de la chaîne de caractères attendue>
    // Format attendu : <aaaa-MM-jj_HH:mm:ss> ZWave_SWITCH_BINARY_2 power: <valeur_numérique> W
    // Les autres formats sont ignorés
    // Lexèmes correspondants : TIMESTAMP ID NOM_OBJET SEPARATEUR VALEUR
    // TIMESTAMP : aaaa-MM-jj_HH:mm:ss
    // ID : ZWave_SWITCH_BINARY_2
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
	traduireLigne = (String ligne, String donnees, int numLigne, BufferedWriter config) -> {
	    if (donnees != null) {
		String [] evenements = donnees.split(SEPARATEUR) ;
		TimeStamp.verifierDate(evenements, nomFichierOriginal, numLigne);
		return evenements[0] + Util.SEPARATEUR + evenements[2].substring(0, evenements[2].length()-1) + Util.SEPARATEUR + evenements[3] ;
	    }
	    return null ;
	} ;
    }

    
    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, TimeStampException {
	Util.traduireFormatOriginalVersFormatOEBL(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, PATTERN_SWITCH2, traduireLigne, new TraducteurTraiterFichierExceptions(nomFichierOriginal, nomFichierOEBL));
	try (BufferedWriter config = new BufferedWriter(new FileWriter(nomFichierConfig))) {
	    config.write("power") ;
	} catch (IOException ex) {
	    throw new ConfigEcrireObjetsException(nomFichierConfig);
	}
    }
    
}
