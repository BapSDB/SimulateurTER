
package traducteur;

import configurateur.FabriqueConfigurateur;
import exceptions.EntreeSortieException;
import exceptions.FichierIntrouvableException;
import exceptions.LireDonneesException;
import exceptions.TimeStampException;
import java.util.Map;
import traducteur.FabriqueTraducteur.TraduireLigne;

public abstract class Traducteur {
    
    protected final FabriqueTraducteur ft ;

    public Traducteur(FabriqueTraducteur ft) {
	this.ft = ft ;
    }
    
    /**
     * Traduit un fichier de traces spécifié dans un fichier au format "One-Event-By-Line"
     * @throws FichierIntrouvableException
     * si le fichier à traduire n'existe pas.
     * @throws EntreeSortieException
     * si une erreur d'entrée/sortie est apparue.
     * @throws LireDonneesException
     * @throws TimeStampException
     * si un timestamp n'a pas pu être parsé ou est incohérent.
     */
    public abstract void traduireFormatOriginalVersFormatOEBL() throws FichierIntrouvableException, EntreeSortieException, LireDonneesException, TimeStampException ;
    
    public Map<String, Integer> getNomsObjets() {
	return ft.nomsObjets;
    }
    
    public String getNomFichierOriginal() {
	return ft.nomFichierOriginal;
    }

    public String getNomFichierOEBL() {
	return ft.nomFichierOEBL;
    }

    public String getNomFichierConfig() {
	return ft.nomFichierConfig;
    }
    
    protected TraduireLigne getTraduireLigne () {
	return ft.traduireLigne ;
    }
    
    public FabriqueConfigurateur nouvelleFabriqueConfigurateur () {
	return new FabriqueConfigurateur(this) ;
    }
    
}