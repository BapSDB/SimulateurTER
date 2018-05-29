package util;

public class Util {
    
    public static final int NB_EVENEMENTS = 2 << 16 ;
    public static final String SEPARATEUR = " ; " ;
    
    private static String[] separerCheminNomFichier (String nomFichier) {
	int i = nomFichier.lastIndexOf("/") ;
	return new String[]{nomFichier.substring(0, i+1), nomFichier.substring(i+1)} ;
    }
    
    private static String[] separerNomFichierExtension (String nomFichier) {
	int i = nomFichier.lastIndexOf(".") ;
	return new String[]{nomFichier.substring(0, i+1), nomFichier.substring(i+1)} ;
    }
    
    
    public static String obtenirNomFichier (String nomFichier) {
	return separerCheminNomFichier(nomFichier)[1] ;
    }
    
    public static String [] obtenirCheminNomFichierExtension (String nomFichier) {
	String [] cheminNomfichier = separerCheminNomFichier(nomFichier) ;
	String [] nomFichierExtension = separerNomFichierExtension(cheminNomfichier[1]) ;
	return new String[]{cheminNomfichier[0], nomFichierExtension[0],  nomFichierExtension[1]} ;
    }
    
}
