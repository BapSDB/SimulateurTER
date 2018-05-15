
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
    private static final String SEPARATEUR = BLANCS ;
    private static final String CHEMIN = "(.+?)(\\.[^.]*$|$)" ; // récupéré depuis http://movingtofreedom.org/2008/04/01/regex-match-filename-base-and-extension/ le mardi 15 mai 2018
    private static final String TOPIC = "\"topic\":\""+CHEMIN+"\"" ;
    private static final String NOM_OBJET = "\"name\":\"[A-Za-z]\\w*\"" ;
    private static final String TYPE = "\"operation\":\"[A-Za-z]\\w*\"" ;
    private static final String VALEUR = "\"message\":\"\\w+\"" ;
    private static final Pattern PATTERN_MQQT = Pattern.compile(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}") ;

    public TraducteurMQTT(String nomFichierOriginal) {
	super(nomFichierOriginal) ;
	traduireLigne = (String ligne, String donnees, int numLigne, BufferedWriter config) -> {
	    if (donnees != null) {
		String [] strings = donnees.split(SEPARATEUR) ;
		TimeStamp.verifierDate(Long.parseUnsignedLong(strings[0]), nomFichierOriginal, numLigne);
		strings = strings[1].substring(1,strings[1].length()-1).split(",") ;
		String name = strings[3].split(":")[1] ;
		return strings[0] + Util.SEPARATEUR + strings[2].substring(0, strings[2].length()-1) + Util.SEPARATEUR + strings[3] ;
	    }
	    return null ;
	} ;
    }

    /**
     *
     * @throws FichierIntrouvableException
     * @throws EntreeSortieException
     * @throws TimeStampException
     */
    @Override
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, TimeStampException {
	Util.traduireFormatOriginalVersFormatOEBL(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, PATTERN_MQQT, traduireLigne, new TraducteurTraiterFichierExceptions(nomFichierOriginal, nomFichierOEBL));
    }
    
}
