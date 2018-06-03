
package simulateur;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static javax.management.timer.Timer.ONE_SECOND;
import static traducteur.Traducteur.AFFICHAGE;

class Ajourneur extends Iterateur {
    
    private final Timer TIMER = new Timer() ;
    private TimerTask tache = new AfficherLigneSuivante() ;
    protected long temps ;
    
    public Ajourneur(List<String> entete, List<List<String>> contenu) {
        super(entete, contenu);
    }
    
    @Override
    public synchronized void lancer () {
        if (etat == Etat.EN_ATTENTE) {
            AFFICHAGE.setAffichage("Affichage d'une ligne de données par intervalle de " + intervalle/ONE_SECOND + " seconde(s)");
            TIMER.scheduleAtFixedRate(tache, intervalle, intervalle);
        }
        else {
            AFFICHAGE.setAffichage("Reprise de l'affichage d'une ligne de données par intervalle de " + intervalle/ONE_SECOND + " seconde(s)");
            TIMER.scheduleAtFixedRate(tache = new AfficherLigneSuivante(), temps, intervalle);
        }
        etat = Etat.LECTURE ;
    }

    @Override
    public synchronized void suspendre() {
        temps = System.currentTimeMillis() % intervalle ;
        AFFICHAGE.setAffichage("Pause de l'affichage d'une ligne de données par intervalle de " + intervalle/ONE_SECOND + " seconde(s)");
        tuerTache();
    }
    
    private void tuerTache () {
        if (etat == Etat.LECTURE) {
            tache.cancel();
            TIMER.purge();
            etat = Etat.PAUSE ;
        }
    }

    @Override
    public synchronized void tuer() {
        TIMER.cancel() ;
        etat = Etat.FIN ;
    }
    
    private class AfficherLigneSuivante extends TimerTask {

        @Override
        synchronized public void run() {
            if(aSuivant())
                suivant();
            else
                tuerTache();
        }
    }
    
}
