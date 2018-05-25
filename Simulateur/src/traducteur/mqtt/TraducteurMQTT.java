
package traducteur.mqtt;

import exceptions.SimulateurException;
import java.util.regex.Pattern;
import traducteur.Traducteur;
import util.TimeStamp;


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
    private static final String VALEUR = "\"message\":\".+\"" ;
    private  static final Pattern PATTERN_MQQT = Pattern.compile(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}") ;
    
    public TraducteurMQTT(FabriqueTraducteurMQTT ft) throws SimulateurException {
	super(ft);
    }

    @Override
    public Pattern getPattern() {
        return PATTERN_MQQT ;
    }

    @Override
    public String getSeparateur() {
        return SEPARATEUR ;
    }

}
