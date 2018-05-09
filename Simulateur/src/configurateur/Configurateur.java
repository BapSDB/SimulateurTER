package configurateur;

import exceptions.fichier_config.ConfigFichierIntrouvableException;
import exceptions.fichier_config.ConfigNomObjetException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Configurateur {
    
    private final Set<String> nomsObjets = new HashSet<>();
    private final List<String> posNomsObjets = new ArrayList() ;
    private static final Pattern SKIP = Pattern.compile("[^\\S\\n]*\\n") ;

    public Configurateur(String nomFichierEntree) throws ConfigNomObjetException, ConfigFichierIntrouvableException {
	lireObjets(nomFichierEntree);
    }

    public List<String> getNomsObjets() {
	return posNomsObjets ;
    }
    
    /**
     * Lit un fichier de configuration listant les objets utilisés dans la simulation
     * 
     * @param nomFichierEntree
     * Le chemin (absolu ou relatif) <p> + nom du fichier de configuration à lire
     * @throws ConfigNomObjetException
     * @throws ConfigFichierIntrouvableException
     */
    public final void lireObjets (String nomFichierEntree) throws ConfigNomObjetException, ConfigFichierIntrouvableException {
	int pos = 0 ;
	try (Scanner scanner = new Scanner(new File(nomFichierEntree))) {
	    scanner.useDelimiter("");
	    int numLigne = 0 ;
	    while (scanner.hasNext()) {
		while (scanner.hasNext(SKIP)) {
		    scanner.skip(SKIP);
		    numLigne++ ;
		}
		if (scanner.hasNext()) {
		    String ligne = scanner.nextLine() ;
		    try(Scanner scannerLigne = new Scanner(ligne)) {
			String nomObjet = scannerLigne.findInLine("\\w+") ;
			numLigne++ ;
			if (nomObjet != null) {
			    posNomsObjets.add(nomObjet);
			    nomsObjets.add(nomObjet);
			} else {
			    throw new ConfigNomObjetException(nomObjet, numLigne, nomFichierEntree) ;
			}
		    }
		}
	    }
	} catch (FileNotFoundException ex) {
	    throw new ConfigFichierIntrouvableException(ex, nomFichierEntree);
	}
    }
}
