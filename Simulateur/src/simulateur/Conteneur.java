
package simulateur;

import java.util.List;

public class Conteneur {
    
    final List<String> entete ;
    final StringBuilder contenu ;

    public Conteneur(List<String> entete, StringBuilder contenu) {
        this.entete = entete;
        this.contenu = contenu;
    }

    public List<String> getEntete() {
        return entete;
    }

    public StringBuilder getContenu() {
        return contenu;
    }
    
}
