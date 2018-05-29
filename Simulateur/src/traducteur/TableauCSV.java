
package traducteur;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import util.ListeChaineeOrdonnee;
import static util.Util.NB_EVENEMENTS;

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
