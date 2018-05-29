package configurateur;

import exceptions.SimulateurException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import exceptions.config.ConfigNomObjetException;
import exceptions.csv.CSVFichierIntrouvableException;
import exceptions.csv.CSVLireDonneesException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import traducteur.TableauCSV;
import traducteur.Traducteur;

public final class Configurateur {
    
    /**
     * Lit un fichier de configuration listant les objets utilisés dans la simulation
     * @param nomFichierEntree
     * Le chemin (absolu ou relatif) <p> + nom du fichier de configuration à lire.
     * @throws ConfigNomObjetException
     * si le un nom d'objet ne respecte pas la convention de nommage.
     * @throws ConfigFichierIntrouvableException
     * si le fichier de configuration n'existe pas.
     * @throws EntreeSortieException
     * si une erreur d'entrée/sortie est apparue
     */
    
    private static void lireFichierConfig(Traducteur traducteur) throws SimulateurException {
        String nomObjet ;
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(traducteur.getNomFichierConfig()))) {
            while ((nomObjet = lineNumberReader.readLine()) != null)
                traducteur.getTableauCSV().getNomsObjets().put(nomObjet, new TableauCSV.PositionPadding(traducteur.getTableauCSV().getNomsObjets().size(), nomObjet.length()));
        } catch (FileNotFoundException ex) {
            throw new ConfigFichierIntrouvableException(traducteur.getNomFichierConfig()) ;
        } catch (IOException ex) {
            throw new ConfigLireObjetsException(traducteur.getNomFichierConfig()) ;
        }
    }
    
    private static void lireFichierOEBL(Traducteur traducteur) throws SimulateurException {
        String ligne ;
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(traducteur.getNomFichierOEBL()))) {
            while ((ligne = lineNumberReader.readLine()) != null) {
                String [] evenement = ligne.split("[^\\S\n]*;[^\\S\n]*");
                traducteur.getTableauCSV().lireValeur(evenement[0], evenement[1], evenement[2]) ;
            }
        } catch (FileNotFoundException ex) {
            throw new CSVFichierIntrouvableException(traducteur.getNomFichierOEBL()) ;
        } catch (IOException ex) {
            throw new CSVLireDonneesException(traducteur.getNomFichierOEBL()) ;
        }
    }    

    public static void lireFormatOEBL(Traducteur traducteur) throws SimulateurException {
        lireFichierConfig(traducteur);
        lireFichierOEBL(traducteur);
    }
    
}
