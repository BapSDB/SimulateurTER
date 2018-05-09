package exceptions;

public class NomObjetException extends SimulateurException {
    
    public NomObjetException(String nomObjet) {
	super("L'objet " + "\"" + nomObjet + "\" a un nom incorrect.") ;
	codeErreur = 3 ;
    }
}
