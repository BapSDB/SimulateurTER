package exceptions;

public abstract class SimulateurException extends Exception {
    
    protected int codeErreur ;

    public SimulateurException() {
    }

    public SimulateurException(String message) {
	super(message);
    }
    
    public void terminerExecutionSimulateur () {
	System.err.println(getMessage());
	System.exit(codeErreur);
    }
    
}
