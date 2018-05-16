
package traducteur.switch2;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import exceptions.config.ConfigEcrireObjetsException;
import exceptions.traducteur.TraducteurTraiterFichierExceptions;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import traducteur.Traducteur;
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
            static final String SEPARATEUR = BLANCS ;
    private static final String NOM_OBJET = "power:" ;
    private static final String VALEUR = "\\d+" ;
    private static final String UNITE = "W" ;
    private static final Pattern PATTERN_SWITCH2 = Pattern.compile(TIMESTAMP+SEPARATEUR+ID+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR+SEPARATEUR+UNITE) ;

    public TraducteurSwitch2(FabriqueTraducteurSwitch2 ft) {
	super(ft);
    }
    
    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	Util.traduireFormatOriginalVersFormatOEBL(PATTERN_SWITCH2, getTraduireLigne(), new TraducteurTraiterFichierExceptions(getNomFichierOriginal(), getNomFichierOEBL(), getNomFichierConfig()));
	try (BufferedWriter config = new BufferedWriter(new FileWriter(getNomFichierConfig()))) {
	    config.write("power") ;
	} catch (IOException ex) {
	    throw new ConfigEcrireObjetsException(getNomFichierConfig());
	}
    }
    
}
