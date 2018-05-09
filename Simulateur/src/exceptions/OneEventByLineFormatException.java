package exceptions;

public class OneEventByLineFormatException extends SimulateurException {

    public OneEventByLineFormatException(int numLigne, String ligne) {
	codeErreur = 2 ;
	message = "La ligne n°" + numLigne + " " + "\"" + ligne + "\"" + " ne respecte pas le format \"One-Event-By-Line\"." ;
    }
    
}
