package configurateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Configurateur {
    
    private final List<String> nomsObjets = new ArrayList<>(128) ;

    public List<String> getNomsObjets() {
	return nomsObjets;
    }
    
    public void lireObjets (String nomFichierEntree) {
	Scanner scanner ;
	try {
	    scanner = new Scanner(new File(nomFichierEntree)) ;
	    scanner.useDelimiter("\\s*\n+");
	    while (scanner.hasNext()) {
		String objet = scanner.next() ;
		if (objet.matches("\\w+"))
		    nomsObjets.add(objet) ;
		else {
		    System.err.println("L'objet " + "\"" + objet + "\" a un nom incorrect.") ;
		    scanner.close();
		    System.exit(2);
		}
	    }
	    System.out.println(nomsObjets);
	    scanner.close();
	} catch (FileNotFoundException ex) {
	    System.err.println("Le fichier de configuration " + nomFichierEntree + " n'existe pas");
	    System.exit(1);
	}
	
    }
    
}
