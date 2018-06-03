
package traducteur;

import java.beans.*;

public class Affichage {
    
    public static final String PROPRIETE_AFFICHAGE = "affichage";
    
    private String affichage;
    
    private final PropertyChangeSupport changeSupport;
    
    public Affichage() {
        this.changeSupport = new PropertyChangeSupport(this);
    }
    
    public String getAffichage() {
        return affichage;
    }
    
    public void setAffichage(String value) {
        String oldValue = affichage;
        affichage = value;
        changeSupport.firePropertyChange(PROPRIETE_AFFICHAGE, oldValue, affichage);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
