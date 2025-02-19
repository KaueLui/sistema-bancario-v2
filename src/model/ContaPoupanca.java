package model;

import java.math.BigDecimal;
import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;
import java.time.LocalDateTime;

public class ContaPoupanca extends ContaBancaria {

    private static final long serialVersionUID = 1L;

    public ContaPoupanca(int numero) {
        super();
        setNumeroConta(numero);
        setSaldo(BigDecimal.ZERO);
        setStatus(true);
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

    public BigDecimal calcularTarifaTransferencia(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.02")); 
    }
}