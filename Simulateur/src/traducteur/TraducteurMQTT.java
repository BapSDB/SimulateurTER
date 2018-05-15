
package traducteur;

import exceptions.traducteur.TraducteurNomObjetNonUniqueException;
import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import exceptions.traducteur.TraducteurTraiterFichierExceptions;
import java.io.BufferedWriter;
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
    private static final String CHEMIN = "(.+?)(\\.[^.\"]*\"|\")" ; // récupéré depuis http://movingtofreedom.org/2008/04/01/regex-match-filename-base-and-extension/ le mardi 15 mai 2018
    private static final String TOPIC = "\"topic\":\""+CHEMIN ;
    private static final String NOM_OBJET = "\"name\":\"[A-Za-z]\\w*\"" ;
    private static final String TYPE = "\"operation\":\"[A-Za-z]\\w*\"" ;
    private static final String VALEUR = "\"message\":\"[\\w\\.]+\"" ;
    private static final Pattern PATTERN_MQQT = Pattern.compile(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}") ;

    public TraducteurMQTT(String nomFichierOriginal) {
	super(nomFichierOriginal) ;
	traduireLigne = (String ligne, String donnees, int numLigne, BufferedWriter config) -> {
	    if (donnees != null) {
		String [] strings = donnees.split(SEPARATEUR) ;
		TimeStamp.verifierDate(Long.parseUnsignedLong(strings[0]), nomFichierOriginal, numLigne);
                String timestamp = strings[0] ;
		strings = strings[1].substring(1,strings[1].length()-1).split(",") ;
		String nomObjet = (nomObjet = strings[1].split(":")[1]).substring(1, nomObjet.length()-1) ;
                String valeur = (valeur = strings[3].split(":")[1]).substring(1, valeur.length()-1) ;
                if (!nomsObjets.containsKey(nomObjet)) {
                    nomsObjets.put(nomObjet, nomsObjets.size());
                    config.write(nomObjet+"\n");
                }
		return timestamp + Util.SEPARATEUR + nomObjet + Util.SEPARATEUR + valeur ;
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
    public void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException {
	Util.traduireFormatOriginalVersFormatOEBL(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, PATTERN_MQQT, traduireLigne, new TraducteurTraiterFichierExceptions(nomFichierOriginal, nomFichierOEBL, nomFichierConfig));
    }
    
    public static void main(String[] args) {
        System.out.println("1440497603216 {\"topic\":\"/a4h/out/Presence_Lit/command\",\"name\":\"Presence_Lit\",\"operation\":\"command\",\"message\":\"OFF\"}".
                replaceFirst(TIMESTAMP+SEPARATEUR+"\\{"+TOPIC+","+NOM_OBJET+","+TYPE+","+VALEUR+"\\}", "true"));
    }
    
}
