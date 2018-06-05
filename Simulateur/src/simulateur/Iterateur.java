
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
    protected PropertyChangeListener ecouteurEtat;
    
    PropertyChangeSupport proprietesIterateur, proprietesEtat ;
    private List<String> precedent ;
    private List<String> suivant ;
    private boolean aSuivant ;
    private boolean aPrecedent ;

    public static enum Etat {EN_ATTENTE, PAUSE, LECTURE, FIN} ;
    public static Etat etat ;
    
    public Iterateur(List<String> entete, List<List<String>> contenu) {
        this.entete = entete;
        this.contenu = java.util.Collections.synchronizedList(contenu);
        this.iterateur = this.contenu.listIterator();
        this.proprietesIterateur = new PropertyChangeSupport(this);
        this.proprietesEtat = new PropertyChangeSupport(this);
        etat = Etat.EN_ATTENTE;
    }
    
    protected Iterateur (Iterateur iterateur) {
        this.entete = iterateur.entete;
        this.contenu = iterateur.contenu;
        this.iterateur = iterateur.iterateur;
        this.proprietesIterateur = iterateur.proprietesIterateur;
        this.proprietesEtat = iterateur.proprietesEtat;
        this.precedent = iterateur.precedent;
        this.suivant = iterateur.suivant;
        this.aSuivant = iterateur.aSuivant;
        this.aPrecedent = iterateur.aPrecedent;
        etat = Etat.EN_ATTENTE;
        mettreAjourIteration();
    }
    
    public final void mettreAjourIteration () {
        setaSuivant(iterateur.hasNext());
        setaPrecedent(iterateur.hasPrevious());
    }
    
    public void lancer () {}
    
    public void suspendre() {}
    
    public void tuer() {}
    
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
        proprietesIterateur.firePropertyChange("aSuivant", ancienneValeur, aSuivant);
    }
    
    protected final void setSuivant(List<String> suivant) {
        this.precedent = this.suivant;
        this.suivant = suivant;
        proprietesIterateur.firePropertyChange("suivant", this.precedent, this.suivant);
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
        proprietesIterateur.firePropertyChange("aPrecedent", ancienneValeur, aPrecedent);
    }
    
    protected final void setPrecedent(List<String> precedent) {
        this.suivant = this.precedent ;
        this.precedent = precedent;
        proprietesIterateur.firePropertyChange("precedent", this.suivant, this.precedent);
    }
    
    protected final void setEtat(Etat etat) {
        Etat ancienneValeur = Iterateur.etat ;
        Iterateur.etat = etat ;
        proprietesEtat.firePropertyChange("etat", ancienneValeur, etat);
        proprietesIterateur.firePropertyChange("etat", ancienneValeur, etat);
    }

    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        proprietesIterateur.addPropertyChangeListener(listener);
    }
    
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        proprietesIterateur.removePropertyChangeListener(listener);
    }
    
    public Iterateur nouvelIterateur (int indice) {
        
        switch(indice) {
            case 0 :
            case 1 : return new Iterateur(this);
            case 2 : return new Simulateur(this);
            case 3 : return new Ajourneur(this);
            default : throw new IllegalStateException();
        }
    }

}
