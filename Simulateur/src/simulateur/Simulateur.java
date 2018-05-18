package simulateur;

import configurateur.FabriqueConfigurateur;
import traducteur.FabriqueTraducteur;
import exceptions.SimulateurException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import exceptions.one_event_by_line.* ;
import java.io.File;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.Map.Entry;
import traducteur.TableauCSV.ListeChaineeOrdonnee;
import traducteur.TableauCSV.ListeChaineeOrdonnee.ListeChaineeOrdonneeIterateur;
import traducteur.TableauCSV.PositionPadding;
import traducteur.TableauCSV.ValeurPosition;
import traducteur.Traducteur;
import util.StringUtil;
import util.Util;

public final class Simulateur {
    
    private final FabriqueSimulateur fs ;
    private static final String JVM = new File(Simulateur.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()
            + FileSystems.getDefault().getSeparator() + ".." + FileSystems.getDefault().getSeparator() + "traces" ; 
    public static final String TRACES = new File(System.getProperty("user.dir")).toPath().relativize(new File(JVM).toPath()) + FileSystems.getDefault().getSeparator() ;
    public static final String OEBL = TRACES + "oebl" + FileSystems.getDefault().getSeparator() ;
    public static final String CSV = TRACES + "csv" + FileSystems.getDefault().getSeparator() ;
    
    public Simulateur(FabriqueSimulateur fs) {
	this.fs = fs ;
    }
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV () throws OneEventByLineEcrireFormatCSVException, ConfigFichierIntrouvableException, ConfigLireObjetsException, OneEventByLineLireDonneesException {
        String nomFichierCSV = CSV+Util.obtenirNomFichier(fs.traducteur.getNomFichierCSV());
	if (!new File(nomFichierCSV).exists()) {
            
            if (fs.traducteur.getTableauCSV().getNomsObjets().isEmpty()) {
                System.out.println(fs.traducteur.getTableauCSV().getNomsObjets().size());
                Simulateur simulateur = new FabriqueConfigurateur(fs.traducteur).creer().nouveauSimulateur().creer() ;
                simulateur.ecrireFormatCSV();
                
            } else {
                
                System.out.println("Création de " + nomFichierCSV + " en cours...");
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierCSV))) {
                    bufferedWriter.write(StringUtil.centrer("timestamp", fs.traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR) ;
                    Set<String> nomsObjets = fs.traducteur.getTableauCSV().getNomsObjets().keySet() ;
                    int n = 0 ;
                    for (String nomObjet : nomsObjets)
                        bufferedWriter.write(StringUtil.centrer(nomObjet, fs.traducteur.getTableauCSV().getNomsObjets().get(nomObjet).getPadding()) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n")) ;
                    for (Entry<String, ListeChaineeOrdonnee<ValeurPosition>> entrySet : fs.traducteur.getTableauCSV().getTableau().entrySet()) {
			System.out.println(entrySet.getValue());
                        n = 0 ;
			Iterator<PositionPadding> positionPadding = fs.traducteur.getTableauCSV().getNomsObjets().values().iterator() ;
			int padding ;
                        bufferedWriter.write(StringUtil.centrer(entrySet.getKey(), fs.traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR);
                        for (ListeChaineeOrdonneeIterateur<ValeurPosition> it = entrySet.getValue().iterator() ;  it.hasNext() ;) {
			    ValeurPosition valeur = it.next() ;
			    padding = positionPadding.next().getPadding() ;
			    while(n < valeur.getPosition()) {
				System.out.println(valeur.getPosition());
				bufferedWriter.write(StringUtil.centrer("", padding) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n"));
				padding = positionPadding.next().getPadding() ;
			    }
			    bufferedWriter.write(StringUtil.centrer(valeur.getValeur(), padding) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n"));
			}
                    }
                    System.out.println("Traduction terminée --> création du fichier " + nomFichierCSV);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                    throw new OneEventByLineEcrireFormatCSVException(nomFichierCSV) ;
                }
            }
	}
        else
            System.out.println("Le fichier " + fs.traducteur.getNomFichierOEBL() + " existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.");
    }
    
    private static void verifierArguments (String [] args) {
	if (args.length < 1) {
	    System.err.println("usage : java simulateur.Simulateur <nom_de_fichier_de_traces.(sw2|mqtt|oebl)");
	    System.exit(99);
	}
    }
    
    private static void traduireFormatOriginalVersFormatCSV (String nomFichierOriginal) {
	try {
	    Traducteur traducteur = FabriqueTraducteur.nouvelleFabrique(nomFichierOriginal).creer() ;
	    traducteur.appliquerTraduction();
	    traducteur.nouvelleFabriqueSimulateur().creer().ecrireFormatCSV() ;
	    //Util.execCommande(new String[]{"cat",traducteur.getNomFichierOEBL()});
	    //Util.execCommande(new String[]{"cat", traducteur.getNomFichierCSV()});
	} catch (SimulateurException ex) {
	    ex.terminerExecutionSimulateur();
	}
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        verifierArguments(args);
        traduireFormatOriginalVersFormatCSV(args[0]);
    }
    
}
