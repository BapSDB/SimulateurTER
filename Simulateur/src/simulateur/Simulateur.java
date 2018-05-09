package simulateur;

import configurateur.Configurateur;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Simulateur {
    
    private final Configurateur configurateur ;

    public Simulateur(Configurateur configurateur) {
	this.configurateur = configurateur;
    }

    public Configurateur getConfigurateur() {
	return configurateur;
    }
    
    private void lireFormatOneEventByLine (String nomFichierEntree) {
	Scanner scanner ;
	try {
	    scanner = new Scanner(new File(nomFichierEntree)) ;
	    Pattern pattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d\\s*;\\s*\\w+\\s*;\\s*\\w+");
	    String [] ligneDecoupee ;
	    int l = 0 ;
	    while (scanner.hasNext()) {
		scanner.skip("\\s*") ;
		String ligne = scanner.findInLine(pattern) ;
		l++ ;
		if (ligne != null) {
		    ligneDecoupee = ligne.split("\\s*;\\s*") ;
		    System.out.println(java.util.Arrays.toString(ligneDecoupee));
		    scanner.nextLine();
		}
		else {
		    System.err.println("La ligne n°" + l + " " + ligne + " ne respecte pas le format \"One-Event-By-Line\".") ;
		    scanner.close();
		    System.exit(2);
		}
	    }
	    //System.out.println(nomsObjets);
	    scanner.close();
	} catch (FileNotFoundException ex) {
	    System.err.println("Le fichier de configuration " + nomFichierEntree + " n'existe pas");
	    System.exit(1);
	}
    }
    
    private void ecrireFormatCSV (String nomFichierSortie) {
	BufferedWriter bufferedWriter ;
	try {
	    bufferedWriter = new BufferedWriter(new FileWriter(nomFichierSortie));
	    bufferedWriter.write("timestamp; ") ;
	    List<String> nomsObjets = configurateur.getNomsObjets() ;
	    int i = 0 ;
	    for (; i < nomsObjets.size()-1; i++) {
		bufferedWriter.write(nomsObjets.get(i) + "; ") ;
	    }
	    bufferedWriter.write(nomsObjets.get(i) + "\n");
	    bufferedWriter.close();
	} catch (IOException ex) {
	    System.err.println("Une erreur est apparue lors de l'écriture des traces au format CSV dans le fichier " + nomFichierSortie + ".") ;
	    System.exit(3);
	}
    }
    
    private void execCommande (String [] cmd) {
	try {
	    Process p = Runtime.getRuntime().exec(cmd);
	    new Thread(() -> {
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())) ;
		    String line ;
		    while((line = br.readLine()) != null)
			System.out.println(line);
		} catch (IOException ex) {
		    Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }).start();
	    p.waitFor() ;
	} catch (IOException | InterruptedException ex) {
	    Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Simulateur simulateur = new Simulateur(new Configurateur()) ;
	simulateur.getConfigurateur().lireObjets("ressources/fichier_config.txt");
	simulateur.ecrireFormatCSV("ressources/fichier_tabulaire.csv");
	simulateur.execCommande(new String[]{"cat","ressources/fichier_tabulaire.csv"});
	simulateur.lireFormatOneEventByLine("ressources/oebl.txt");
    }
    
}
