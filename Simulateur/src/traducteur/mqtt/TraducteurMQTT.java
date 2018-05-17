
package traducteur.mqtt;

import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import exceptions.traducteur.TraducteurTraiterFichierExceptions;
import java.util.regex.Pattern;
import traducteur.Traducteur;
import util.TimeStamp;
import util.Util;
import static simulateur.Simulateur.OEBL;


public class TraducteurMQTT extends Traducteur {

    // Définition des expressions régulières
    // Pour l'analyse d'une ligne d'un fichier "MQTT"
    // <nature de la chaîne de caractères attendue>
    // Format attendu : <timestamp_type_unsigned_long_long> {"topic":"<chemin>/<operation>","name":"<nom_operation>","operation":"<type_operation>","message":"<valeur_alphanumérique>"}
    // Lexèmes correspondants : TIMESTAMP SEPARATEUR SUJET NOM_OBJET TYPE VALEUR
    // TIMESTAMP : unsigned_long_long_long
    // SEPARATEUR : blanc(s)
    // SUJET : chemin de l'opération
    // NOM_OBJET : une chaîne de caractères alphanumérique commençant par une lettre
    // VALEUR : une chaîne de caractères alphanumérique
    
    private static final String TIMESTAMP = TimeStamp.FORMAT_UNSIGNED_LONG_LONG ;
    private static final String BLANCS = "[^\\S\\n]+" ;
            static final String SEPARATEUR = BLANCS ;
    private static final String CHEMIN = "(.+?)(\\.[^.\"]*\"|\")" ; // récupéré depuis http://movingtofreedom.org/2008/04/01/regex-match-filename-base-and-extension/ le mardi 15 mai 2018
    private static final String TOPIC = "\"topic\":\""+CHEMIN ;
    private static final String NOM_OBJET = "\"name\":\"[A-Za-z]\\w*\"" ;
    private static final String TYPE = "\"operation\":\"[A-Za-z]\\w*\"" ;
    private static final String VALEUR = "\"message\":\"[\\w\\.]+\"" ;
    private static final Pattern PATTERN_MQQT = Pattern.compile(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}") ;
    
    public TraducteurMQTT(FabriqueTraducteurMQTT ft) {
	super(ft);
    }

    /**
     *
     * @throws FichierIntrouvableException
     * @throws EntreeSortieException
     * @throws LireDonneesException
     * @throws TimeStampException
     */
    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	Util.traduireFormatOriginalVersFormatOEBL(PATTERN_MQQT, getTraduireLigne(), new TraducteurTraiterFichierExceptions(getNomFichierOriginal(), OEBL+Util.obtenirNomFichier(getNomFichierOEBL()), OEBL+Util.obtenirNomFichier(getNomFichierConfig())));
    }
    
    /*public static void main(String[] args) {
        System.out.println("1440497603216 {\"topic\":\"/a4h/out/Presence_Lit/command\",\"name\":\"Presence_Lit\",\"operation\":\"command\",\"message\":\"OFF\"}".
                replaceFirst(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}", "true"));
    }*/

    @Override
    public boolean estOEBL() {
	return false ;
    }

}
