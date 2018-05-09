package exceptions;

public class NomObjetException extends SimulateurException {
    
    public NomObjetException(String nomObjet) {
	codeErreur = 3 ;
	message = "L'objet " + "\"" + nomObjet + "\" a un nom incorrect." ;
    }
}
