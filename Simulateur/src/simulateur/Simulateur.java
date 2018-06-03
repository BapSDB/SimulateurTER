
package simulateur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.List;
import static traducteur.Traducteur.AFFICHAGE;
import static traducteur.Traducteur.convertisseur;

class Simulateur extends Iterateur {
    
    private final Thread SIMULATION ;
    private PropertyChangeListener ecouteurSimulation ;
    
    public Simulateur(List<String> entete, List<List<String>> contenu) {
        super(entete, contenu);
        SIMULATION = new Thread(new Simulation()) ;
        if (ecouteurSimulation != null)
            changeSupport.removePropertyChangeListener(ecouteurSimulation) ;
        changeSupport.addPropertyChangeListener(this::interrompreSimulation) ;
    }
    
    @Override
    public synchronized void lancer() {
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
    public synchronized void suspendre() {
        AFFICHAGE.setAffichage("Simulation en Pause") ;
        setEtat(Etat.PAUSE);
    }
    
    @Override
    public synchronized void tuer() {
        setEtat(Etat.FIN);
    }
    
    private synchronized void interrompreSimulation (PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("etat")) {
            SIMULATION.interrupt() ;
        }
    }
    
    private class Simulation implements Runnable {
        
        private long date, temps ;
        private List<String> donnees ;
        
        private synchronized boolean afficherPremiereLigne () {
            donnees = iterateur.next() ;
            try {
                date = convertisseur.parse(donnees.get(0)).getTime() ;
            } catch (ParseException ex) {
                ex.printStackTrace(System.err);
                return false ;
            }
            setSuivant(donnees);
            mettreAjourIteration();
            return true ;
        }
        
        private void attendreDelai () throws InterruptedException {
            Thread.sleep(temps) ;
        }

        private boolean afficherProchaineLigne () {
            
            donnees = iterateur.next() ;
            
            try {
                temps = convertisseur.parse(donnees.get(0)).getTime() - date ;
            } catch (ParseException ex) {
                ex.printStackTrace(System.err);
                return false ;
            }
            
            try {
                attendreDelai();
            } catch (InterruptedException ex) {
                try {
                    synchronized(this) {
                        wait() ;
                    }
                    temps = System.currentTimeMillis() - temps ;
                    attendreDelai();
                } catch (InterruptedException ex1) {
                    if (etat == Etat.LECTURE)
                        synchronized(this) {
                            notify() ;
                        }
                    else
                        return false ;
                }
            }
            setSuivant(donnees);
            mettreAjourIteration();
            return true ;
        }
        
        
        @Override
        public synchronized void run() {
            
            if (aSuivant())
                if(!afficherPremiereLigne())
                    return ;

            while (aSuivant())
                    if (!afficherProchaineLigne())
                        return ;
                    
        }
    }
}
