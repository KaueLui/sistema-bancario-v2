package model;

import java.math.BigDecimal;
import java.util.List;

import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;


public interface IConta {
	
		void depositar(BigDecimal quantia) throws ValorInvalidoException;

		void transferir(IConta destino, BigDecimal quantia) throws ContaInexistenteException, SaldoInsuficienteException, ValorInvalidoException;
   
		void sacar(BigDecimal quantia) throws SaldoInsuficienteException, ValorInvalidoException;
	
    	boolean isStatus();

    	Integer getNumeroConta();
     
    	BigDecimal getSaldo();
    
    	void setSaldo(BigDecimal saldo); 

    	void imprimirExtratoConta(int mes, int year);
    	 
    	List<RegistroTransacao> getTransacoes();
}