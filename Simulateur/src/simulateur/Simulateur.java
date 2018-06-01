
package simulateur;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class Simulateur {
    
    public static final Timer TIMER = new Timer() ;
    public TimerTask TACHE;
    public static long intervalle = -1 ;
    protected boolean started ;
    protected final List<String> entete;
    protected final List<List<String>> contenu;
    protected ListIterator<List<String>> iterateur;
    
    PropertyChangeSupport changeSupport;
    private List<String> suivant;
    private List<String> precedent;
    private boolean aSuivant ;
    private boolean aPrecedent ;
    
    public Simulateur(List<String> entete, List<List<String>> contenu) {
        this.entete = entete;
        this.contenu = contenu;
        this.iterateur = this.contenu.listIterator();
        this.changeSupport = new PropertyChangeSupport(this);
    }
    
    public void mettreAjourIteration () {
        setaSuivant(iterateur.hasNext());
        setaPrecedent(iterateur.hasPrevious());
    }
    
    synchronized public void tuerTache() {
        if (started) {
            TACHE.cancel();
            TIMER.purge();
        }
    }
    
    synchronized public void lancer () {}
    
    synchronized public void suspendre() {}
    
    public List<String> getEntete() {
        return entete;
    }
    
    public List<List<String>> getContenu() {
        return contenu ;
    }

    public ListIterator<List<String>> getIterateur() {
        return iterateur;
    }

    public void setIterateur(ListIterator<List<String>> iterateur) {
        this.iterateur = iterateur;
    }

    public void suivant() {
        setSuivant(iterateur.next());
        mettreAjourIteration();
    }
    
    public final boolean isaSuivant() {
        return aSuivant;
    }

    private void setaSuivant(boolean aSuivant) {
        boolean ancienneValeur = this.aSuivant;
        this.aSuivant = aSuivant;
        changeSupport.firePropertyChange("aSuivant", ancienneValeur, aSuivant);
    }
    
    public List<String> getSuivant () {
        return suivant ;
    }

    private void setSuivant(List<String> suivant) {
        List<String> ancienneValeur = this.suivant;
        this.suivant = suivant;
        changeSupport.firePropertyChange("suivant", ancienneValeur, suivant);
    }
    
    public void precedent() {
        setPrecedent(iterateur.previous());
        mettreAjourIteration();
    }
    
    public final boolean isaPrecedent() {
        return aPrecedent;
    }

    private void setaPrecedent(boolean aPrecedent) {
        boolean ancienneValeur = this.aPrecedent;
        this.aPrecedent = aPrecedent;
        changeSupport.firePropertyChange("aPrecedent", ancienneValeur, aPrecedent);
    }
    
    public List<String> getPrecedent () {
        return precedent ;
    }
    
    private void setPrecedent(List<String> precedent) {
        List<String> ancienneValeur = this.precedent;
        this.precedent = precedent;
        changeSupport.firePropertyChange("precedent", ancienneValeur, precedent);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public Simulateur nouvelAjourneur() {
        Ajourneur ajourneur = new Ajourneur(entete, contenu);
        ajourneur.iterateur = this.iterateur ;
        ajourneur.started = this.started ;
        return ajourneur ;
    }
    
    

}
