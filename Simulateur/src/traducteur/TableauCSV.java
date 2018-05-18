
package traducteur;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableauCSV {
    
    private static class NomObjet implements Comparable<NomObjet> {
	
	private final String nomObjet ;
	private final Integer position ;
	private int padding ;

	public NomObjet(String nomObjet, int position) {
	    this.nomObjet = nomObjet;
	    this.position = position;
	}

	@Override
	public int compareTo(NomObjet o) {
	    return position.compareTo(o.position) ;
	}
	
	
    }
    
    private static class ListeChaineeOrdonnee<T> {
	
	private static class Maillon<T> {
	    
	    private T element ;
	    private Maillon<T> suivant ;

	    public Maillon(T element, Maillon<T> suivant) {
		this.element = element;
		this.suivant = suivant;
	    }
	    
	}
	
	private int size ;
	private final Maillon<T> fictif ;
	private final Comparator<T> comparator ;

	public ListeChaineeOrdonnee(Comparator<T> comparator) {
	    fictif = new Maillon<>(null, null) ;
	    fictif.suivant = fictif ;
	    this.comparator = comparator ;
	}
	
	public void inserer (T element) {
	    
	    Maillon<T> courant = fictif.suivant ;
	    Maillon<T> precedent = null ;
	    while (courant != fictif && comparator.compare(element, courant.element) >= 0) {
		precedent = courant ;
		courant = courant.suivant ;
	    }
	    
	    Maillon<T> maillon = new Maillon<>(element, courant) ;
	    
	    if (precedent != null)
		precedent.suivant = maillon ;
	    else
		fictif.suivant = maillon ;
	}

	@Override
	public String toString() {
	    StringBuilder str = new StringBuilder("[") ;
	    Maillon<T> courant = fictif.suivant ;
	    if (courant != fictif) {
		str.append(courant.element.toString()) ;
		courant = courant.suivant ;
	    }
	    while(courant != fictif) {
		str.append(", ").append(courant.element.toString()) ;
		courant = courant.suivant ;
	    }
	    return str.append("]").toString() ;
	}
	
	public static void main(String[] args) {
	    List<Integer> l = new Random().ints(1, 0, 1).boxed().collect(Collectors.toList()) ;
	    ListeChaineeOrdonnee<Integer> lc = new ListeChaineeOrdonnee<>(Comparator.<Integer>naturalOrder()) ;
	    System.out.println(l);
	    l.forEach((Integer t) -> {
		lc.inserer(t);
	    });
	    System.out.println(lc);
	}
	
    } 
    
    private static final int NB_EVENEMENTS = 2 << 16 ;
    private final Set<String> nomsObjets = new LinkedHashSet<>() ;
    private final Map<String, ListeChaineeOrdonnee<NomObjet>> tableau = new LinkedHashMap<>(NB_EVENEMENTS) ;
    
    public void ecrireNomObjet (String nomObjet, BufferedWriter config) throws IOException {
        if (!nomsObjets.contains(nomObjet)) {
            nomsObjets.add(nomObjet);
            config.write(nomObjet+"\n");
        }
    }
    
    public void lireValeur (String timestamp, String nomObjet, String valeur) {
        tableau.putIfAbsent(timestamp, new ListeChaineeOrdonnee<>(Comparator.<NomObjet>naturalOrder()));
        tableau.get(timestamp).put(nomsObjets.get(nomObjet), valeur) ;
    }
    
}
