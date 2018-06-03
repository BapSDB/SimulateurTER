
package simulateur;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.ListIterator;

public class Iterateur {
    
    public static long intervalle = -1 ;
    protected final List<String> entete;
    protected final List<List<String>> contenu;
    protected ListIterator<List<String>> iterateur;
    
    PropertyChangeSupport changeSupport;
    private List<String> suivant;
    private List<String> precedent;
    private boolean aSuivant ;
    private boolean aPrecedent ;
    public static enum Etat {EN_ATTENTE, PAUSE, LECTURE, FIN} ;
    public static Etat etat ;
    
    public Iterateur(List<String> entete, List<List<String>> contenu) {
        this.entete = entete;
        this.contenu = contenu;
        this.iterateur = this.contenu.listIterator();
        this.changeSupport = new PropertyChangeSupport(this);
        etat = Etat.EN_ATTENTE;
    }
    
    public final void mettreAjourIteration () {
        setaSuivant(iterateur.hasNext());
        setaPrecedent(iterateur.hasPrevious());
    }
    
    public synchronized void lancer () {}
    
    public synchronized void suspendre() {}
    
    public synchronized void tuer() {}
    
    public final List<String> getEntete() {
        return entete;
    }
    
    public final List<List<String>> getContenu() {
        return contenu ;
    }

    public final ListIterator<List<String>> getIterateur() {
        return iterateur;
    }

    public final void setIterateur(ListIterator<List<String>> iterateur) {
        this.iterateur = iterateur;
    }

    public final List<String> suivant() {
        List<String> donnees ;
        setSuivant(donnees = iterateur.next());
        mettreAjourIteration();
        return donnees;
    }
    
    public final boolean aSuivant() {
        return aSuivant;
    }

    protected final void setaSuivant(boolean aSuivant) {
        boolean ancienneValeur = this.aSuivant;
        this.aSuivant = aSuivant;
        changeSupport.firePropertyChange("aSuivant", ancienneValeur, aSuivant);
    }
    
    protected final void setSuivant(List<String> suivant) {
        List<String> ancienneValeur = this.suivant;
        this.suivant = suivant;
        changeSupport.firePropertyChange("suivant", ancienneValeur, suivant);
    }
    
    public final List<String> precedent() {
        List<String> donnees ;
        setPrecedent(donnees = iterateur.previous());
        mettreAjourIteration();
        return donnees ;
    }
    
    public final boolean aPrecedent() {
        return aPrecedent;
    }

    protected final void setaPrecedent(boolean aPrecedent) {
        boolean ancienneValeur = this.aPrecedent;
        this.aPrecedent = aPrecedent;
        changeSupport.firePropertyChange("aPrecedent", ancienneValeur, aPrecedent);
    }
    
    protected final void setPrecedent(List<String> precedent) {
        List<String> ancienneValeur = this.precedent;
        this.precedent = precedent;
        changeSupport.firePropertyChange("precedent", ancienneValeur, precedent);
    }
    
    protected final void setEtat(Etat etat) {
        Etat ancienneValeur = Iterateur.etat ;
        Iterateur.etat = etat ;
        changeSupport.firePropertyChange("etat", ancienneValeur, etat);
    }

    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public final Iterateur nouvelAjourneur() {
        Ajourneur ajourneur = new Ajourneur(entete, contenu);
        ajourneur.iterateur = this.iterateur ;
        return ajourneur ;
    }
    
    public final Iterateur nouveauSimulateur() {
        Simulateur simulateur = new Simulateur(entete, contenu);
        simulateur.iterateur = this.iterateur ;
        return simulateur ;
    }

}
