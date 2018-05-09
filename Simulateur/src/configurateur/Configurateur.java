package configurateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Configurateur {
    
    private final NavigableMap<Integer, String> nomsObjets = new TreeMap<>(Comparator.naturalOrder());

    public Configurateur(String nomFichierEntree) {
	lireObjets(nomFichierEntree);
    }
    

    public Collection<String> getNomsObjets() {
	return nomsObjets.values() ;
    }
    
    public final void lireObjets (String nomFichierEntree) {
	int pos = 0 ;
	try (Scanner scanner = new Scanner(new File(nomFichierEntree))) {
	    scanner.useDelimiter("\\s*\n+");
	    while (scanner.hasNext()) {
		String nomObjet = scanner.next() ;
		if (nomObjet.matches("\\w+"))
		    nomsObjets.put(pos++, nomObjet) ;
		else {
		    System.err.println("L'objet " + "\"" + nomObjet + "\" a un nom incorrect.") ;
		    scanner.close();
		    System.exit(2);
		}
	    }
	} catch (FileNotFoundException ex) {
	    System.err.println("Le fichier de configuration " + nomFichierEntree + " n'existe pas");
	    System.exit(1);
	}
	
    }
    
}
