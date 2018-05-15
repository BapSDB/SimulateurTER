package simulateur;

public class Evenement {
    
    static final String SEPARATEUR = " ; " ;
    
    private final TimeStamp timeStamp ;
    private final String nomObjet ;
    private final String valeur ;
    
    
    public Evenement(String [] ligneDecoupee) throws TimeStampParseException {
        timeStamp = new TimeStamp(ligneDecoupee[0]) ;
	nomObjet = ligneDecoupee[1] ;
	valeur = ligneDecoupee[2] ;
    }

    public TimeStamp getTimestamp() {
        return timeStamp;
    }

    public String getNomObjet() {
        return nomObjet;
    }

    public String getValeur() {
        return valeur;
    }
    
    @Override
    public String toString() {
	return timeStamp + SEPARATEUR + nomObjet + SEPARATEUR + valeur ;
    }
    
}
