package configurateur;

import exceptions.EntreeSortieException;
import exceptions.LireDonneesException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import exceptions.config.ConfigNomObjetException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import simulateur.FabriqueSimulateur;

public final class Configurateur {
    
    private final FabriqueConfigurateur fc ;
    
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
    public Configurateur(FabriqueConfigurateur fc) throws ConfigFichierIntrouvableException, ConfigLireObjetsException {
	this.fc = fc ;
        String nomObjet ;
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fc.traducteur.getNomFichierConfig()))) {
            while ((nomObjet = lineNumberReader.readLine()) != null)
                fc.traducteur.getNomsObjets().put(nomObjet, fc.traducteur.getNomsObjets().size());
        } catch (IOException ex) {
            throw new ConfigLireObjetsException(fc.traducteur.getNomFichierConfig()) ;
        }
    }    
    
    public FabriqueSimulateur nouveauSimulateur () {
	return new FabriqueSimulateur(fc.traducteur) ;
    }
    
}
