
package traducteur;

import java.beans.*;

public class AffichageBean {
    
    public static final String PROPRIETE_AFFICHAGE = "affichage";
    
    private String affichage;
    
    private final PropertyChangeSupport propertySupport;
    
    public AffichageBean() {
        this.propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getAffichage() {
        return affichage;
    }
    
    public void setAffichage(String value) {
        String oldValue = affichage;
        affichage = value;
        propertySupport.firePropertyChange(PROPRIETE_AFFICHAGE, oldValue, affichage);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
