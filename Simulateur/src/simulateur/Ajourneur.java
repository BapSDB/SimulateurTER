
package simulateur;

import java.util.List;
import java.util.TimerTask;

class Ajourneur extends Simulateur {
    
    private long temps ;
    
    public Ajourneur(List<String> entete, List<List<String>> contenu) {
        super(entete, contenu);
        TACHE = new AfficherLigneSuivante();
    }
    
    @Override
    synchronized public void lancer () {
        if (!started) {
            TIMER.scheduleAtFixedRate(TACHE, intervalle, intervalle);
            started = true;
        }
        else
            TIMER.scheduleAtFixedRate(TACHE = new AfficherLigneSuivante(), temps, intervalle);
    }

    @Override
    synchronized public void suspendre() {
        temps = System.currentTimeMillis() % intervalle ;
        tuerTache();
    }

    protected class AfficherLigneSuivante extends TimerTask {

        @Override
        synchronized public void run() {
            if(isaSuivant())
                suivant();
            else
                tuerTache();
        }
    }
    
}
