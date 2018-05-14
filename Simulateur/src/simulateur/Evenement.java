package simulateur;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evenement {
    
    static final String SEPARATEUR = " ; " ;
    
    private Date timestamp ;
    private final String nomObjet ;
    private final String valeur ;
    
    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public Evenement(String [] ligneDecoupee) {
	try {
	    timestamp = TIMESTAMP_FORMAT.parse(ligneDecoupee[0]) ;
	} catch (ParseException ex) {
	    Logger.getLogger(Evenement.class.getName()).log(Level.SEVERE, null, ex);
	}
	nomObjet = ligneDecoupee[1] ;
	valeur = ligneDecoupee[2] ;
    }

    @Override
    public String toString() {
	return TIMESTAMP_FORMAT.format(timestamp) + SEPARATEUR + nomObjet + SEPARATEUR + valeur ;
    }
    
}
