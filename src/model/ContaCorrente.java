package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;

//Classe que representa uma conta corrente, herdando de ContaBancaria
public class ContaCorrente extends ContaBancaria {

    private static final long serialVersionUID = 1L;

    // Construtor para ContaCorrente inicializa com um número de conta dado
    public ContaCorrente(int numero) {
        super();
        setNumeroConta(numero);
    }
	@Override
    public void transferir(IConta destino, BigDecimal valor) throws ContaInexistenteException, SaldoInsuficienteException, ValorInvalidoException {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("Valor inválido para transferência.");  
        }

        if (!this.isStatus()) {
            throw new ContaInexistenteException("Não é possível transferir de uma conta inativa.");
        }

        if (!destino.isStatus()) {
            throw new ContaInexistenteException("Não é possível transferir para uma conta inativa.");
        }

        BigDecimal taxa = calcularTarifaTransferencia(valor);
        BigDecimal valorComTaxa = valor.add(taxa);

        if (valorComTaxa.compareTo(this.getSaldo()) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência.");
        }

        this.setSaldo(this.getSaldo().subtract(valorComTaxa));
        destino.setSaldo(destino.getSaldo().add(valor));

       
        this.getTransacoes().add(new RegistroTransacao(valorComTaxa, TipoTransacao.TRANSACAO_DEBITO, LocalDateTime.now()));
        destino.getTransacoes().add(new RegistroTransacao(valor, TipoTransacao.TRANSACAO_CREDITO, LocalDateTime.now()));

        System.out.println("Transferência realizada com sucesso. Taxa: " + taxa);
    }

	// Método que calcula a tarifa de transferência específica para conta corrente.
    public BigDecimal calcularTarifaTransferencia(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.03")); 
    } 
}