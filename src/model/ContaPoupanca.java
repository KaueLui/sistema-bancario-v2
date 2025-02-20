package model;

import java.math.BigDecimal;
import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;
import java.time.LocalDateTime;

// Classe que representa uma conta poupança, herdando de ContaBancaria
public class ContaPoupanca extends ContaBancaria {

    private static final long serialVersionUID = 1L;

    public ContaPoupanca(int numero) {
        super();
        setNumeroConta(numero);
        setSaldo(BigDecimal.ZERO);
        setStatus(true);
    }

    // Método sobrescrito para realizar transferências entre contas
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
        
        // Calcula a tarifa de transferência, e abaixo calcula o valor com a tarifa que vai ser debitado.
        BigDecimal taxa = calcularTarifaTransferencia(valor);
        BigDecimal valorComTaxa = valor.add(taxa);

        if (valorComTaxa.compareTo(this.getSaldo()) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência.");
        }

        // Atualiza o saldo da conta origem, subtraindo o valor total e atualiza o valor na conta de destino.
        this.setSaldo(this.getSaldo().subtract(valorComTaxa));
        destino.setSaldo(destino.getSaldo().add(valor));
        
        // Registra a transação de débito na conta origem (valor + taxa) e atualiza o valor na conta de destino.
        this.getTransacoes().add(new RegistroTransacao(valorComTaxa, TipoTransacao.TRANSACAO_DEBITO, LocalDateTime.now()));
        destino.getTransacoes().add(new RegistroTransacao(valor, TipoTransacao.TRANSACAO_CREDITO, LocalDateTime.now()));

        System.out.println("Transferência realizada com sucesso. Taxa: " + taxa);
    }
	
	// Método que calcula a tarifa de transferência específica para conta poupança
    public BigDecimal calcularTarifaTransferencia(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.02")); 
    }
}