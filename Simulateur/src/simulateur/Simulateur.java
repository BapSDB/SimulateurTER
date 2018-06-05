
package simulateur;

import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.List;
import static traducteur.Traducteur.AFFICHAGE;
import static traducteur.Traducteur.convertisseur;
import static util.TimeStamp.convertirDureeVersString;

final class Simulateur extends Iterateur {
    
    private Thread SIMULATION ;
    
    public Simulateur(List<String> entete, List<List<String>> contenu) {
        super(entete, contenu);
        initialiser();
    }
    
    protected Simulateur (Iterateur iterateur) {
        super(iterateur);
        initialiser();
    }
    
    private void initialiser () {
        SIMULATION = new Thread(new Simulation()) ;
        etat = Etat.EN_ATTENTE ;
        if (ecouteurEtat != null)
            proprietesEtat.removePropertyChangeListener(ecouteurEtat);
        proprietesEtat.addPropertyChangeListener(ecouteurEtat = this::ecouterEtat) ;
    }
    
    @Override
    public void lancer() {
        if (etat == Etat.EN_ATTENTE) {
            AFFICHAGE.setAffichage("Lancement de la simulation") ;
            SIMULATION.start() ;
            etat = Etat.LECTURE ;
        }
        else {
            AFFICHAGE.setAffichage("Reprise de la simulation") ;
            setEtat(Etat.LECTURE);
        }
    }

    @Override
    public void suspendre() {
        setEtat(Etat.PAUSE);
        AFFICHAGE.setAffichage("Simulation en Pause") ;
    }
    
    @Override
    public void tuer() {
        setEtat(Etat.FIN);
    }
    
    private void ecouterEtat (PropertyChangeEvent evt) {
        SIMULATION.interrupt() ;
    }
    
    private class Simulation implements Runnable {
        
        private long date, timestamp ;
        private List<String> donnees ;
        private int i ;
        
        private long convertirDate (long datePrecedente) {
            try {
                return (date = convertisseur.parse(donnees.get(0)).getTime()) - datePrecedente ;
            } catch (ParseException ex) {
                return (date = Long.parseUnsignedLong(donnees.get(0).trim())) - datePrecedente ;
            }
            
        }
        
        private long convertirDate (String date) {
            try {
                return convertisseur.parse(date).getTime() ;
            } catch (ParseException ex) {
                return Long.parseUnsignedLong(date.trim()) ;
            }
            
        }
        
        private boolean afficherPremiereLigne (boolean premierTimestamp) {
            timestamp = convertirDate(date) ;
            if (!premierTimestamp) {
                AFFICHAGE.setAffichage("Prochaine MAJ : " + convertirDureeVersString(timestamp));
                if (!afficherLigne())
                    return false ;
            }
            iterateur.next() ;
            setSuivant(donnees);
            mettreAjourIteration();
            i = iterateur.nextIndex() ;
            return true ;
        }
        
        private void attendreDelai () throws InterruptedException {
            Thread.sleep(timestamp) ;
        }
        
        private synchronized boolean afficherLigne () {
            long tempsEcoule = System.nanoTime() ;
            try {
                attendreDelai();
            } catch (InterruptedException ex) {
                try {
                    if (etat == Etat.PAUSE) {
                        timestamp -= ((System.nanoTime() - tempsEcoule)  / 1_000_000l) ;
                        wait() ;
                    }
                    else
                        return false ;
                } catch (InterruptedException ex1) {
                    if (etat == Etat.LECTURE) {
                        notify() ;
                        AFFICHAGE.setAffichage("Temps Restant : " + convertirDureeVersString(timestamp));
                        afficherLigne() ;
                    }
                    else
                        return false ;
                }
            }
            return true ;
        }

        private boolean afficherProchaineLigne () {
            donnees = contenu.get(i) ;
            timestamp = convertirDate(date) ;
            AFFICHAGE.setAffichage("Prochaine MAJ : " + convertirDureeVersString(timestamp));
            if (!afficherLigne())
                return false ;
            iterateur.next() ;
            setSuivant(donnees);
            mettreAjourIteration();
            i = iterateur.nextIndex() ;
            return true ;
        }
        
        
        @Override
        public void run() {
            
            i = iterateur.nextIndex() ;
            donnees = contenu.get(i) ;
            date = i == 0 ? 0l : convertirDate(contenu.get(i-1).get(0)) ;
            
            if (aSuivant()) {
                if(!afficherPremiereLigne(i == 0)) {
                    AFFICHAGE.setAffichage("Fin de la simulation") ;
                    return ;
                }
            }

            while (aSuivant())
                if (!afficherProchaineLigne()) {
                    AFFICHAGE.setAffichage("Fin de la simulation") ;
                    return ;
                }
            
            AFFICHAGE.setAffichage("Fin de la simulation") ;
        }
    }
}
