package simulateur;

import exceptions.SimulateurException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import exceptions.one_event_by_line.* ;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    
    private Traducteur traducteur ;
    private static final String JVM = new File(Simulateur.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()
            + FileSystems.getDefault().getSeparator() + ".." + FileSystems.getDefault().getSeparator() + "traces" ; 
    public static final String TRACES = new File(System.getProperty("user.dir")).toPath().relativize(new File(JVM).toPath()) + FileSystems.getDefault().getSeparator() ;
    public static final String OEBL = TRACES + "oebl" + FileSystems.getDefault().getSeparator() ;
    public static final String CSV = TRACES + "csv" + FileSystems.getDefault().getSeparator() ;
    
    private String[] entete;
    private final StringBuilder contenu = new StringBuilder() ;
    
    public Simulateur(Traducteur traducteur) {
	
        this.traducteur = traducteur ;
        
        String nomFichierCSV = CSV+Util.obtenirNomFichier(traducteur.getNomFichierCSV()) ;
        
        try {
            if (!new File(nomFichierCSV).exists()) {
                ecrireFormatCSV(nomFichierCSV);
                //Util.execCommande(new String[]{"cat",traducteur.getNomFichierOEBL()});
                //Util.execCommande(new String[]{"cat", traducteur.getNomFichierCSV()});
            }
            else {
                traducteur.getConsole().append("Le fichier ").append(traducteur.getNomFichierOEBL()).append(" existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.").append(nomFichierCSV).append("\n") ;
                traducteur.getConsole().append("Chargement du fichier ").append(nomFichierCSV).append("...\n") ;
                lireFormatCSV(nomFichierCSV);
                traducteur.getConsole().append("Chargement terminé.\n");
            }
        } catch (SimulateurException ex) {
            ex.terminerExecutionSimulateur();
        }
    }
    
    private void lireFormatCSV (String nomFichierCSV) throws ConfigFichierIntrouvableException, ConfigLireObjetsException {
        String ligne ;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nomFichierCSV))) {
            entete = bufferedReader.readLine().split(";") ;
            while ((ligne = bufferedReader.readLine()) != null)
                contenu.append(ligne).append("\n");
        } catch (FileNotFoundException ex) {
            throw  new ConfigFichierIntrouvableException(nomFichierCSV);
        } catch (IOException ex) {
            throw new ConfigLireObjetsException(nomFichierCSV);
        }
    }
    
    /**
     * Ecrit dans un fichier au format CSV les données sous une forme tabulaire
     * @throws OneEventByLineEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV (String nomFichierCSV) throws OneEventByLineEcrireFormatCSVException, ConfigFichierIntrouvableException, ConfigLireObjetsException, OneEventByLineLireDonneesException {
        traducteur.getConsole().append("Création de ").append(nomFichierCSV).append(" en cours...\n");
        String seqChar ;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierCSV))) {
            seqChar = StringUtil.centrer("timestamp", traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR ;
            bufferedWriter.write(seqChar);
            contenu.append(seqChar);
            Set<String> nomsObjets = traducteur.getTableauCSV().getNomsObjets().keySet() ;
            int n = 0 ;
            for (String nomObjet : nomsObjets) {
                seqChar = StringUtil.centrer(nomObjet, traducteur.getTableauCSV().getNomsObjets().get(nomObjet).getPadding()) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                bufferedWriter.write(seqChar) ;
                contenu.append(seqChar) ;
            }
            for (Entry<String, ListeChaineeOrdonnee<ValeurPosition>> entrySet : traducteur.getTableauCSV().getTableau().entrySet()) {
                n = 0 ;
                Iterator<PositionPadding> positionPadding = traducteur.getTableauCSV().getNomsObjets().values().iterator() ;
                int padding ;
                seqChar = StringUtil.centrer(entrySet.getKey(), traducteur.getTableauCSV().getPaddingTimeStamp()) + Util.SEPARATEUR ;
                bufferedWriter.write(seqChar);
                contenu.append(seqChar);
                for (ListeChaineeOrdonneeIterateur<ValeurPosition> it = entrySet.getValue().iterator() ;  it.hasNext() ;) {
                    ValeurPosition valeur = it.next() ;
                    padding = positionPadding.next().getPadding() ;
                    while(n++ < valeur.getPosition()) {
                        seqChar = StringUtil.centrer("", padding) + (n < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                        bufferedWriter.write(seqChar);
                        contenu.append(seqChar);
                        padding = positionPadding.next().getPadding() ;
                    }
                    seqChar = StringUtil.centrer(valeur.getValeur(), padding) + (n++ < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                    bufferedWriter.write(seqChar);
                    contenu.append(seqChar);
                }
                for (; positionPadding.hasNext() ;) {
                    PositionPadding pp = positionPadding.next() ;
                    seqChar = StringUtil.centrer("", pp.getPadding()) + (pp.getPosition() < nomsObjets.size() - 1 ? Util.SEPARATEUR : "\n") ;
                    bufferedWriter.write(seqChar);
                    contenu.append(seqChar);
                }
            }
            traducteur.getConsole().append("Traduction terminée --> création du fichier ").append(nomFichierCSV).append("\n");
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new OneEventByLineEcrireFormatCSVException(nomFichierCSV) ;
        }
    }

    public Traducteur getTraducteur() {
        return traducteur;
    }
    
    public void setTraducteur(Traducteur traducteur) {
        this.traducteur = traducteur;
    }

    public String[] getEntete() {
        return entete;
    }
    
    public StringBuilder getContenu() {
        return contenu;
    }
    
    
    /**
     * @param args the command line arguments
     */
    
    /*public static void main(String[] args) {
        verifierArguments(args);
        traduireFormatOriginalVersFormatCSV(args[0]);
    }*/
    
}
