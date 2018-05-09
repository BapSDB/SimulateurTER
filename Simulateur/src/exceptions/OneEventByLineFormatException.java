package exceptions;

public class OneEventByLineFormatException extends SimulateurException {

    public OneEventByLineFormatException(int numLigne, String ligne, String nomFichierEntree) {
	codeErreur = 2 ;
	message = "La ligne n°" + numLigne + " " + "\"" + ligne + "\"" + " du fichier " + nomFichierEntree + " ne respecte pas le format \"One-Event-By-Line\"." ;
    }
    
}
