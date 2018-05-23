package simulateur;

import exceptions.SimulateurException;
import exceptions.config.ConfigFichierIntrouvableException;
import exceptions.config.ConfigLireObjetsException;
import exceptions.oebl.OEBLEcrireDonneesFormatCSVException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import traducteur.TableauCSV.PositionPadding;
import traducteur.TableauCSV.ValeurPosition;
import traducteur.Traducteur;
import util.ListeChaineeOrdonnee;
import util.ListeChaineeOrdonnee.ListeChaineeOrdonneeIterateur;
import util.StringUtil;
import util.Util;

public final class Simulateur {
    
    private Traducteur traducteur ;
    private static final String JVM = new File(Simulateur.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()
            + FileSystems.getDefault().getSeparator() + ".." + FileSystems.getDefault().getSeparator() + "traces" ; 
    public static final String TRACES = new File(System.getProperty("user.dir")).toPath().relativize(new File(JVM).toPath()) + FileSystems.getDefault().getSeparator() ;
    public static final String OEBL = TRACES + "oebl" + FileSystems.getDefault().getSeparator() ;
    public static final String CSV = TRACES + "csv" + FileSystems.getDefault().getSeparator() ;
    
    private List<String> entete;
    private final StringBuilder contenu = new StringBuilder() ;
    
    public Simulateur(Traducteur traducteur) {
	
        this.traducteur = traducteur ;
        
        String nomFichierCSV = CSV+Util.obtenirNomFichier(traducteur.getNomFichierCSV()) ;
        
        try {
            if (!new File(nomFichierCSV).exists())
                ecrireFormatCSV(nomFichierCSV);
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
            entete = new ArrayList<>(Arrays.asList(bufferedReader.readLine().split(";"))) ;
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
     * @throws OEBLEcrireFormatCSVException
     * si une erreur est apparue lors de l'écriture du fichier
     * @since V1
     * @see lireFormatOneEventByLine
     */
    
    public void ecrireFormatCSV (String nomFichierCSV) throws SimulateurException {
        traducteur.getConsole().append("Création de ").append(nomFichierCSV).append(" en cours...\n");
        String seqChar ;
        entete = new ArrayList<>(traducteur.getTableauCSV().getNomsObjets().size() + 1) ;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nomFichierCSV))) {
            seqChar = StringUtil.centrer("Timestamp", traducteur.getTableauCSV().getPaddingTimeStamp()) ;
            entete.add(seqChar);
            bufferedWriter.write(seqChar + Util.SEPARATEUR);
            Set<String> nomsObjets = traducteur.getTableauCSV().getNomsObjets().keySet() ;
            int n = 0 ;
            for (String nomObjet : nomsObjets) {
                seqChar = StringUtil.centrer(nomObjet, traducteur.getTableauCSV().getNomsObjets().get(nomObjet).getPadding()) + (++n < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                entete.add(seqChar);
                bufferedWriter.write(seqChar) ;
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
            throw new OEBLEcrireDonneesFormatCSVException(nomFichierCSV) ;
        }
    }

    public Traducteur getTraducteur() {
        return traducteur;
    }
    
    public void setTraducteur(Traducteur traducteur) {
        this.traducteur = traducteur;
    }

    public List<String> getEntete() {
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
