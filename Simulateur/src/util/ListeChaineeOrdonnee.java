
package util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ListeChaineeOrdonnee<T> implements Iterable<T> {

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
            for (Integer integer : lc) {
                System.out.println(integer);
            }
	}
	
	public static class ListeChaineeOrdonneeIterateur<E> implements Iterator<E> {
	    
	    Maillon<E> courant, fictif ;

	    public ListeChaineeOrdonneeIterateur(ListeChaineeOrdonnee<E> listeChaineeOrdonnee) {
		fictif = listeChaineeOrdonnee.fictif ;
		courant = listeChaineeOrdonnee.fictif ;
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
