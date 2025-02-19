package exception;

public class ContaInexistenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ContaInexistenteException(String erro) {
		super(erro);
	}

}
