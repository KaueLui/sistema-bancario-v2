package exception;

public class InputMismatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InputMismatchException(String erro) {
		super(erro);
	}

}