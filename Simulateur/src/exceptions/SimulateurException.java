package exceptions;

public abstract class SimulateurException extends Exception {
    
    private static final boolean MODE_DEBUG = true ;
    protected int codeErreur ;

    public SimulateurException() {
    }

    public SimulateurException(String message) {
	super(message);
    }
    
    public void terminerExecutionSimulateur () {
        if (MODE_DEBUG)
            printStackTrace(System.err);
        else
            System.out.println(getMessage());
	System.exit(codeErreur);
    }
    
}
