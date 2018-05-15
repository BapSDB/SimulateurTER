
package exceptions.traducteur;

import exceptions.TraiterFichierExceptions;

public class TraducteurTraiterFichierExceptions extends TraiterFichierExceptions {
    public TraducteurTraiterFichierExceptions(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig) {
	super(new TraducteurFichierIntrouvableException(nomFichierOriginal), new TraducteurTraduireFichierOriginalVersFichierOEBLException(nomFichierOriginal, nomFichierOEBL, nomFichierConfig));
    }
}
