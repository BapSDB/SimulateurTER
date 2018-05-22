package simulateur;

import configurateur.FabriqueConfigurateur;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import traducteur.TableauCSV.ListeChaineeOrdonnee;
import traducteur.TableauCSV.ListeChaineeOrdonnee.ListeChaineeOrdonneeIterateur;
import traducteur.TableauCSV.PositionPadding;
import traducteur.TableauCSV.ValeurPosition;
import traducteur.Traducteur;
import util.StringUtil;
import util.Util;

public final class Simulateur {
    
    private Traducteur traducteur ;
    private static final String JVM = new File(Simulateur.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()
            + FileSystems.getDefault().getSeparator() + ".." + FileSystems.getDefault().getSeparator() + "traces" ; 
    public static final String TRACES = new File(System.getProperty("user.dir")).toPath().relativize(new File(JVM).toPath()) + FileSystems.getDefault().getSeparator() ;
    public static final String OEBL = TRACES + "oebl" + FileSystems.getDefault().getSeparator() ;
    public static final String CSV = TRACES + "csv" + FileSystems.getDefault().getSeparator() ;
    
    public Simulateur(Traducteur traducteur) {
	
        this.traducteur = traducteur ;
        
        //System.out.println("Le fichier " + traducteur.getNomFichierOEBL() + " existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.");
        //traducteur.getContenu().append("Le fichier ").append(traducteur.getNomFichierOEBL()).append(" existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.").append(nomFichierCSV).append("\n") ;
        
        try {
            ecrireFormatCSV();
	    //Util.execCommande(new String[]{"cat",traducteur.getNomFichierOEBL()});
	    //Util.execCommande(new String[]{"cat", traducteur.getNomFichierCSV()});
	} catch (SimulateurException ex) {
	    ex.terminerExecutionSimulateur();
	}
        
    }
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV () throws OneEventByLineEcrireFormatCSVException, ConfigFichierIntrouvableException, ConfigLireObjetsException, OneEventByLineLireDonneesException {
        String nomFichierCSV = CSV+Util.obtenirNomFichier(traducteur.getNomFichierCSV());
	if (!new File(nomFichierCSV).exists()) {
            System.out.println("Création de " + nomFichierCSV + " en cours...") ;
            traducteur.getContenu().append("Création de ").append(nomFichierCSV).append(" en cours...\n");
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierCSV))) {
                bufferedWriter.write(StringUtil.centrer("timestamp", traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR) ;
                Set<String> nomsObjets = traducteur.getTableauCSV().getNomsObjets().keySet() ;
                int n = 0 ;
                for (String nomObjet : nomsObjets)
                    bufferedWriter.write(StringUtil.centrer(nomObjet, traducteur.getTableauCSV().getNomsObjets().get(nomObjet).getPadding()) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n")) ;
                for (Entry<String, ListeChaineeOrdonnee<ValeurPosition>> entrySet : traducteur.getTableauCSV().getTableau().entrySet()) {
                    n = 0 ;
                    Iterator<PositionPadding> positionPadding = traducteur.getTableauCSV().getNomsObjets().values().iterator() ;
                    int padding ;
                    bufferedWriter.write(StringUtil.centrer(entrySet.getKey(), traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR);
                    for (ListeChaineeOrdonneeIterateur<ValeurPosition> it = entrySet.getValue().iterator() ;  it.hasNext() ;) {
                        ValeurPosition valeur = it.next() ;
                        padding = positionPadding.next().getPadding() ;
                        while(n++ < valeur.getPosition()) {
                            bufferedWriter.write(StringUtil.centrer("", padding) + (n < nomsObjets.size() ? Util.SEPARATEUR : "\n"));
                            padding = positionPadding.next().getPadding() ;
                        }
                        bufferedWriter.write(StringUtil.centrer(valeur.getValeur(), padding) + (n++ < nomsObjets.size() ? Util.SEPARATEUR : "\n"));
                    }
                    positionPadding.forEachRemaining((pp) -> { 
                        try {
                            bufferedWriter.write(StringUtil.centrer("", pp.getPadding()) + (pp.getPosition() < nomsObjets.size() - 1 ? Util.SEPARATEUR : "\n"));
                        } catch (IOException ex) {
                            Logger.getLogger(Simulateur.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                System.out.println("Traduction terminée --> création du fichier " + nomFichierCSV);
                traducteur.getContenu().append("Traduction terminée --> création du fichier ").append(nomFichierCSV).append("\n");
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                throw new OneEventByLineEcrireFormatCSVException(nomFichierCSV) ;
            }
        }
        else {
            System.out.println("Le fichier " + traducteur.getNomFichierOEBL() + " existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.");
            traducteur.getContenu().append("Le fichier ").append(traducteur.getNomFichierOEBL()).append(" existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.").append(nomFichierCSV).append("\n") ;
        }
    }

    public Traducteur getTraducteur() {
        return traducteur;
    }
    
    public void setTraducteur(Traducteur traducteur) {
        this.traducteur = traducteur;
    }
    
    /**
     * @param args the command line arguments
     */
    
    /*public static void main(String[] args) {
        verifierArguments(args);
        traduireFormatOriginalVersFormatCSV(args[0]);
    }*/
    
}
