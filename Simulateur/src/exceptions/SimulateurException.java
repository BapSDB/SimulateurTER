package exceptions;

public abstract class SimulateurException extends Exception {
    
    protected int codeErreur ;
    protected String message ;

    public SimulateurException() {
    }
    
    public void terminerExecutionSimulateur () {
	System.err.println(message);
	System.exit(codeErreur);
    }
    
}
