
package traducteur;

import exceptions.SimulateurException;
import exceptions.traducteur.TraducteurFichierIntrouvableException;
import exceptions.traducteur.TraducteurFormatFichierInconnuException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import static simulateur.Simulateur.CSV;
import static simulateur.Simulateur.OEBL;
import traducteur.mqtt.FabriqueTraducteurMQTT;
import traducteur.oebl.FabriqueTraducteurOEBL;
import traducteur.sw2.FabriqueTraducteurSwitch2;
import util.Util;

public abstract class FabriqueTraducteur {
    
    protected String nomFichierOriginal ;
    protected String nomFichierOEBL ;
    protected String nomFichierConfig ;
    protected String nomFichierCSV ;
    protected final StringBuilder console = new StringBuilder() ;
    
    /**
     * Permet au {@link Traducteur} de spécifier la traduction d'une ligne d'un fichier
     */
    @FunctionalInterface
    public interface TraduireLigne {
	/**
	 * 
	 * @param ligne
	 * la ligne de données à traduire <p>
	 * @param donnees
	 * la ligne de données parsées <p>
	 * @param numLigne
	 * le n° de la ligne en cours de traduction <p>
	 * @param config
	 * permet d'écrire un nouveau nom d'objet dans le fichier de configuration <p>
	 * @return la ligne de données traduite
	 * @throws TimeStampException 
	 * si un timestamp n'a pas pu être parsé ou est incohérent.
	 * @throws IOException
	 * @throws LireDonneesException
	 */
	public String traduireLigne(String ligne, String donnees, int numLigne, BufferedWriter config) throws SimulateurException ;
    }
    
    protected TraduireLigne traduireLigne ;
    protected TableauCSV tableauCSV = new TableauCSV() ;

    protected FabriqueTraducteur(String nomFichierOriginal, String nomFichierOEBL, String nomFichierConfig, String nomFichierCSV) {
	this.nomFichierOriginal = nomFichierOriginal;
	this.nomFichierOEBL = nomFichierOEBL;
	this.nomFichierConfig = nomFichierConfig;
	this.nomFichierCSV = nomFichierCSV;
    }
    
    public static FabriqueTraducteur nouvelleFabrique (String nomFichierOriginal) throws SimulateurException {
	
	if (!new File(nomFichierOriginal).exists())
	    throw new TraducteurFichierIntrouvableException(nomFichierOriginal);
	
	String [] cheminNomFichierExtension = Util.obtenirCheminNomFichierExtension(nomFichierOriginal) ;
	String nomFichierOEBL = OEBL + cheminNomFichierExtension[1] + "oebl" ;
	String nomFichierConfig = OEBL + cheminNomFichierExtension[1] + "config" ;
	String nomFichierCSV = CSV + cheminNomFichierExtension[1] + "csv" ;
	
	switch (cheminNomFichierExtension[2]) {
	    case "sw2" :
		return new FabriqueTraducteurSwitch2(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV) ;
	    case "mqtt" :
		return new FabriqueTraducteurMQTT(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV) ;
	    case "oebl" :
		return new FabriqueTraducteurOEBL(nomFichierOriginal, nomFichierOEBL, nomFichierConfig, nomFichierCSV) ;
	    default :
		throw new TraducteurFormatFichierInconnuException(nomFichierOriginal) ;
	}
	
    }
    
    public abstract Traducteur creer() throws SimulateurException ; 
    
}
