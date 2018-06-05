
package simulateur;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static traducteur.Traducteur.AFFICHAGE;
import static util.TimeStamp.convertirDureeVersString;

final class Ajourneur extends Iterateur {
    
    private final Timer TIMER = new Timer() ;
    private TimerTask tache = new AfficherLigneSuivante() ;
    protected long temps ;
    
    public Ajourneur(List<String> entete, List<List<String>> contenu) {
        super(entete, contenu);
    }
    
    protected Ajourneur (Iterateur iterateur) {
        super(iterateur);
    }
    
    @Override
    public void lancer () {
        if (etat == Etat.EN_ATTENTE) {
            AFFICHAGE.setAffichage("Affichage d'une ligne de données par intervalle de " + convertirDureeVersString(intervalle));
            TIMER.scheduleAtFixedRate(tache, intervalle, intervalle);
        }
        else {
            AFFICHAGE.setAffichage("Reprise de l'affichage d'une ligne de données par intervalle de " + convertirDureeVersString(intervalle));
            TIMER.scheduleAtFixedRate(tache = new AfficherLigneSuivante(), temps, intervalle);
        }
        setEtat(Etat.LECTURE) ;
    }

    @Override
    public void suspendre() {
        temps = System.currentTimeMillis() % intervalle ;
        AFFICHAGE.setAffichage("Pause de l'affichage d'une ligne de données par intervalle de " + convertirDureeVersString(intervalle));
        tuerTache();
    }
    
    private void tuerTache () {
        tache.cancel();
        TIMER.purge();
        setEtat(aSuivant() ? Etat.PAUSE : Etat.FIN) ;
    }

    @Override
    public void tuer() {
        AFFICHAGE.setAffichage("Fin de l'affichage d'une ligne de données par intervalle de " + convertirDureeVersString(intervalle));
        TIMER.cancel() ;
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
