
package traducteur;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TableauCSV {
    
    public static class ValeurPosition implements Comparable<ValeurPosition> {
	
	private final String valeur ;
	private final Integer position ;

	public ValeurPosition(String valeur, int position) {
	    this.valeur = valeur;
	    this.position = position;
	}

	@Override
	public int compareTo(ValeurPosition o) {
	    return position.compareTo(o.position) ;
	}

	public String getValeur() {
	    return valeur;
	}

	public Integer getPosition() {
	    return position;
	}

	@Override
	public String toString() {
	    return "ValeurPosition{" + "valeur=" + valeur + ", position=" + position + '}';
	}

    }
    
    public static class PositionPadding {
	
	private final Integer position ;
	private int padding ;

	public PositionPadding(Integer position, int padding) {
	    this.position = position;
	    this.padding = padding;
	}

	public Integer getPosition() {
	    return position;
	}

	public int getPadding() {
	    return padding;
	}
	
    }
    
    public static class ListeChaineeOrdonnee<T> implements Iterable<T> {

	private static class Maillon<T> {
	    
	    private final T element ;
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
	    size++ ;
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
	    List<Integer> l = new Random().ints(10, 0, 10).boxed().collect(Collectors.toList()) ;
	    ListeChaineeOrdonnee<Integer> lc = new ListeChaineeOrdonnee<>(Comparator.<Integer>naturalOrder()) ;
	    System.out.println(l);
	    l.forEach((Integer t) -> {
		lc.inserer(t);
	    });
	    System.out.println(lc);
	}
	
	public static class ListeChaineeOrdonneeIterateur<E> implements Iterator<E> {
	    
	    Maillon<E> courant, fictif ;

	    public ListeChaineeOrdonneeIterateur(ListeChaineeOrdonnee listeChaineeOrdonnee) {
		fictif = listeChaineeOrdonnee.fictif ;
		courant = listeChaineeOrdonnee.fictif.suivant ;
	    }

	    @Override
	    public boolean hasNext() {
		return courant.suivant != fictif ;
	    }

	    @Override
	    public E next() {
		courant = courant.suivant ;
		return courant.element ;
	    }
	    
	}
	
	@Override
	public ListeChaineeOrdonneeIterateur<T> iterator() {
	    return new ListeChaineeOrdonneeIterateur<>(this) ;
	}
	
	
    } 
    
    private static final int NB_EVENEMENTS = 2 << 16 ;
    private final Map<String, PositionPadding> nomsObjets = new LinkedHashMap<>() ;
    private final Map<String, ListeChaineeOrdonnee<ValeurPosition>> tableau = new LinkedHashMap<>(NB_EVENEMENTS) ;
    private int paddingTimeStamp ;
    
    public void ecrireNomObjet (String nomObjet, BufferedWriter config) throws IOException {
        if (!nomsObjets.containsKey(nomObjet)) {
            nomsObjets.put(nomObjet, new PositionPadding(nomsObjets.size(), nomObjet.length())) ;
            config.write(nomObjet+"\n") ;
        }
    }
    
    public void lireValeur (String timestamp, String nomObjet, String valeur) {
        tableau.putIfAbsent(timestamp, new ListeChaineeOrdonnee<>(Comparator.<ValeurPosition>naturalOrder()));
	PositionPadding positionPadding = nomsObjets.get(nomObjet) ;
	positionPadding.padding = Math.max(positionPadding.padding, valeur.length()) ;
	ValeurPosition valeurPosition = new ValeurPosition(valeur, positionPadding.position) ;
        tableau.get(timestamp).inserer(valeurPosition);
	paddingTimeStamp = Math.max(paddingTimeStamp, timestamp.length());
    }

    public Map<String, PositionPadding> getNomsObjets() {
	return nomsObjets;
    }

    public Map<String, ListeChaineeOrdonnee<ValeurPosition>> getTableau() {
	return tableau;
    }
    
    public int getPaddingTimeStamp() {
	return paddingTimeStamp;
    }

    public void setPaddingTimeStamp(int paddingTimeStamp) {
	this.paddingTimeStamp = paddingTimeStamp;
    }
    
}
