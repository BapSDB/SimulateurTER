
package traducteur.oebl;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import exceptions.traducteur.TraducteurTraduireFichierOriginalVersFichierOEBLException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import traducteur.Traducteur;
import util.TimeStamp;

public class TraducteurOEBL extends Traducteur {
    
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

    public TraducteurOEBL(FabriqueTraducteurOEBL ft) {
	super(ft);
    }

    @Override
    public void appliquerTraduction() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
        super.appliquerTraduction();
        try {
            Files.delete(new File(getNomFichierOriginal()).toPath());
        } catch (IOException ex) {
            throw new TraducteurTraduireFichierOriginalVersFichierOEBLException(getNomFichierOriginal(), getNomFichierOEBL(), getNomFichierCSV());
        }
    }
    
    @Override
    public boolean estOEBL() {
	return true ;
    }

    @Override
    public Pattern getPattern() {
        return PATTERN_ONE_EVENT_BY_LINE ;
    }

    @Override
    public String getSeparateur() {
        return SEPARATEUR ;
    }
    
    @Override
    public String getTimeStamp() {
        return TIMESTAMP ;
    }
    
}
