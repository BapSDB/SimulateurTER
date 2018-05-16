
package exceptions.one_event_by_line;

import exceptions.TraiterFichierExceptions;

public class OneEventByLineTraiterFichierExceptions extends TraiterFichierExceptions {
    
    public OneEventByLineTraiterFichierExceptions(String nomFichierEntreeOEBL) {
	super(new OneEventByLineFichierIntrouvableException(nomFichierEntreeOEBL), new OneEventByLineLireDonneesException(nomFichierEntreeOEBL));
    }
    
}
