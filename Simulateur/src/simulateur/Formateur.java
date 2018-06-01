package simulateur;

import exceptions.SimulateurException;
import exceptions.csv.CSVEcrireDonneesException;
import exceptions.csv.CSVFichierIntrouvableException;
import exceptions.csv.CSVLireDonneesException;
import static traducteur.Traducteur.AFFICHAGE_BEAN;
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
import static util.Util.NB_EVENEMENTS;

public final class Formateur {
    
    private Traducteur traducteur ;
    
    private List<String> entete ;
    private final List<List<String>> contenu = new ArrayList<>(NB_EVENEMENTS) ;
    
    private static final String JVM = new File(Simulateur.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()
            + FileSystems.getDefault().getSeparator() + ".." + FileSystems.getDefault().getSeparator() + "traces" ; 
    public static final String TRACES = new File(System.getProperty("user.dir")).toPath().relativize(new File(JVM).toPath()) + FileSystems.getDefault().getSeparator() ;
    public static final String OEBL = TRACES + "oebl" + FileSystems.getDefault().getSeparator() ;
    public static final String CSV = TRACES + "csv" + FileSystems.getDefault().getSeparator() ;
    
    public static Formateur nouveauFormateur (Traducteur traducteur) throws SimulateurException {
        return new Formateur(traducteur) ;
    }
    
    private Formateur(Traducteur traducteur) throws SimulateurException {
        this.traducteur = traducteur ;
        ecrireOulireFormatCSV();
    }
    
    private void ecrireOulireFormatCSV () throws SimulateurException {
        
        String nomFichierCSV = CSV+Util.obtenirNomFichier(traducteur.getNomFichierCSV()) ;
            
        if (!new File(nomFichierCSV).exists())
            ecrireFormatCSV(nomFichierCSV);
        else {
            AFFICHAGE_BEAN.setAffichage("Le fichier " + traducteur.getNomFichierOEBL() + " existe déjà au format \"Comma-Separated Values\" --> \u00c9tape de traduction ignorée.\n") ;
            AFFICHAGE_BEAN.setAffichage("Chargement du fichier " + nomFichierCSV + "...\n") ;
            lireFormatCSV(nomFichierCSV);
        }
        AFFICHAGE_BEAN.setAffichage("Chargement terminé.\n");
    }
    
    private void lireFormatCSV (String nomFichierCSV) throws SimulateurException {
        String ligne ;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nomFichierCSV))) {
            entete = new ArrayList<>(Arrays.asList(bufferedReader.readLine().split(";"))) ;
            while ((ligne = bufferedReader.readLine()) != null)
                contenu.add(new ArrayList<>(Arrays.asList(ligne.split(";"))));
        } catch (FileNotFoundException ex) {
            throw  new CSVFichierIntrouvableException(nomFichierCSV);
        } catch (IOException ex) {
            throw new CSVLireDonneesException(nomFichierCSV);
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
        AFFICHAGE_BEAN.setAffichage("Création de " + nomFichierCSV + " en cours...\n");
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
                ArrayList<String> donnees = new ArrayList<>(entete.size());
                donnees.add(seqChar);
                for (ListeChaineeOrdonneeIterateur<ValeurPosition> it = entrySet.getValue().iterator() ;  it.hasNext() ;) {
                    ValeurPosition valeur = it.next() ;
                    padding = positionPadding.next().getPadding() ;
                    while(n++ < valeur.getPosition()) {
                        seqChar = StringUtil.centrer("", padding) + (n < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                        bufferedWriter.write(seqChar);
                        donnees.add(seqChar);
                        padding = positionPadding.next().getPadding() ;
                    }
                    seqChar = StringUtil.centrer(valeur.getValeur(), padding) + (n++ < nomsObjets.size() ? Util.SEPARATEUR : "\n") ;
                    bufferedWriter.write(seqChar);
                    donnees.add(seqChar);
                }
                for (; positionPadding.hasNext() ;) {
                    PositionPadding pp = positionPadding.next() ;
                    seqChar = StringUtil.centrer("", pp.getPadding()) + (pp.getPosition() < nomsObjets.size() - 1 ? Util.SEPARATEUR : "\n") ;
                    bufferedWriter.write(seqChar);
                    donnees.add(seqChar);
                }
                
            }
            AFFICHAGE_BEAN.setAffichage("Traduction terminée --> création du fichier " + nomFichierCSV + "\n");
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new CSVEcrireDonneesException(nomFichierCSV) ;
        }
    }

    public Traducteur getTraducteur() {
        return traducteur;
    }
    
    public void setTraducteur(Traducteur traducteur) {
        this.traducteur = traducteur;
    }

    public Simulateur nouveauSimulateur (int indice) {
        
        switch(indice) {
            case 0 :
            case 1 : return new Simulateur(entete, contenu);
            case 2 : return new Ajourneur(entete, contenu);
            default : throw new IllegalStateException();
        }
    }

    
    /**
     * @param args the command line arguments
     */
    
    /*public static void main(String[] args) {
        verifierArguments(args);
        traduireFormatOriginalVersFormatCSV(args[0]);
    }*/
    
}
