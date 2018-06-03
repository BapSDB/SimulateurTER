
package traducteur.sw2;

import exceptions.SimulateurException;
import exceptions.config.ConfigEcrireObjetsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import traducteur.TableauCSV;
import traducteur.Traducteur;
import util.TimeStamp;

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
    private static final Pattern PATTERN_SW2 = Pattern.compile(TIMESTAMP+SEPARATEUR+ID+SEPARATEUR+NOM_OBJET+SEPARATEUR+VALEUR+SEPARATEUR+UNITE) ;
    
    private static final SimpleDateFormat CONVERTISSEUR = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss") ;

    public TraducteurSwitch2(FabriqueTraducteurSwitch2 ft) throws SimulateurException {
	super(ft);
    }
    
    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws SimulateurException {
        getTableauCSV().getNomsObjets().put("power", new TableauCSV.PositionPadding(0, "power".length()));
        super.traduireFormatOriginalVersFormatOEBL() ;
	try (BufferedWriter config = new BufferedWriter(new FileWriter(getNomFichierConfig()))) {
	    config.write("power") ;
	} catch (IOException ex) {
	    throw new ConfigEcrireObjetsException(getNomFichierConfig());
	}
    }
    
    @Override
    public Pattern getPattern() {
        return PATTERN_SW2 ;
    }

    @Override
    public String getSeparateur() {
        return SEPARATEUR ;
    }

    @Override
    public SimpleDateFormat getConvertisseur() {
        return  CONVERTISSEUR ;
    }
    
}
