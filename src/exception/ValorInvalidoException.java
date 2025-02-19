package exception;

public class ValorInvalidoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ValorInvalidoException(String erro) {
		super(erro);
	}

}
