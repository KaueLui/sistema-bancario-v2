package exception;

public class PersistenciaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PersistenciaException(String message) {
        super(message);
    }
	
	public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

}
