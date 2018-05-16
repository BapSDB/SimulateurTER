package exceptions;

import java.util.Arrays;

public abstract class SimulateurException extends Exception {
    
    protected int codeErreur ;

    public SimulateurException() {
    }

    public SimulateurException(String message) {
	super(message);
    }
    
    public void terminerExecutionSimulateur () {
	printStackTrace(System.err);
	System.exit(codeErreur);
    }
    
}
