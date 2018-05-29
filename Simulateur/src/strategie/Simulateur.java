
package strategie;

import java.util.Collection;

public class Simulateur {
    
    private final Collection<String> entete;
    private final Collection<Collection<String>> contenu;

    public Simulateur(Collection<String> entete, Collection<Collection<String>> contenu) {
        this.entete = entete;
        this.contenu = contenu;
    }

    public Collection<String> getEntete() {
        return entete;
    }
    
    public Collection<Collection<String>> getContenu() {
        return contenu ;
    }
    
}
