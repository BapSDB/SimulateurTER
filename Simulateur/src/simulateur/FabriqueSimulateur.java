
package simulateur;

import configurateur.Configurateur;
import exceptions.one_event_by_line.OneEventByLineFormatException;
import exceptions.one_event_by_line.OneEventByLineNomObjetIntrouvableException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import util.TimeStamp;
import util.Util.AjouterElement;
import static simulateur.Simulateur.SEPARATEUR ;

public class FabriqueSimulateur {
    
    static final int NB_EVENEMENTS = 2 << 16 ;
    Configurateur configurateur ;
    int [] padding ;
    Map<String, String[]> tableau ;
    
    final AjouterElement ajouterElement = (String ligne, String donnees, int numLigne) -> {
	if (donnees != null) {
	    String [] evenement = donnees.split(SEPARATEUR) ;
	    if (configurateur.getNomsObjets().contains(evenement[1])) {
		int indiceNomObjet = configurateur.getNomObjetVersIndice(evenement[1]) ;
		tableau.putIfAbsent(evenement[0], new String[configurateur.getNomsObjets().size()]);
		tableau.get(evenement[0])[indiceNomObjet] = evenement[2] ;
		padding[indiceNomObjet + 1] = Math.max(padding[indiceNomObjet + 1], evenement[2].length()) ;
	    }
	    else
		throw new OneEventByLineNomObjetIntrouvableException(evenement[1], numLigne, configurateur.getNomFichierOEBL());
	}
	else {
	    throw new OneEventByLineFormatException(ligne, numLigne, configurateur.getNomFichierOEBL());
	}
    };

    public FabriqueSimulateur(Configurateur configurateur) {
	this.configurateur = configurateur;
	
	padding = new int [configurateur.getNomsObjets().size()+1] ;
	padding[0] = Math.max("timestamp".length(), TimeStamp.LONGUEUR_FORMAT_DATE_HEURE) ;
	Iterator<String> it = configurateur.getNomsObjets().iterator() ;
	for (int i = 1 ; it.hasNext() ; i++)
	    padding[i] = it.next().length() ;
        tableau = new LinkedHashMap<>(NB_EVENEMENTS) ;
    }
    
    public Simulateur creer () {
	return new Simulateur(this) ;
    }
}
