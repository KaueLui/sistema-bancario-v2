package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;

public abstract class ContaBancaria implements IConta, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer numeroConta;
	private BigDecimal saldo;
	private LocalDateTime dataAbertura;
	private boolean status;
	List<RegistroTransacao> transacoes;

	// Construtor da ContaBancaria. 
	public ContaBancaria() {
		this.numeroConta = new Random().nextInt(100);
		this.saldo = BigDecimal.ZERO;
		saldo.setScale(4, RoundingMode.HALF_UP);
		this.dataAbertura = LocalDateTime.now();
		this.status = true;
		transacoes = new ArrayList<>();
	}

	public Integer getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(Integer numeroConta) {
		this.numeroConta = numeroConta;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<RegistroTransacao> getTransacoes() {
		return transacoes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroConta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaBancaria other = (ContaBancaria) obj;
		return Objects.equals(numeroConta, other.numeroConta);
	}
	
	// Realiza um depósito na conta.
	@Override
    public void depositar(BigDecimal quantia) throws ValorInvalidoException {
        if (status) {
            if (quantia.compareTo(BigDecimal.ZERO) > 0) {
                this.saldo = this.saldo.add(quantia);
                transacoes.add(new RegistroTransacao(quantia, TipoTransacao.CREDITO, LocalDateTime.now()));
                System.out.println("Depósito realizado com sucesso.");
            } else {
                throw new ValorInvalidoException("Valor inválido para depósito.");
            }
        } else {
            throw new ContaInexistenteException("Conta inativa.");
        }
    }

	// Realiza um saque na conta.
	@Override
    public void sacar(BigDecimal quantia) throws SaldoInsuficienteException, ValorInvalidoException {
        if (status) {
            if (quantia.compareTo(BigDecimal.ZERO) > 0) {
                if (this.saldo.compareTo(quantia) >= 0) {
                    this.saldo = this.saldo.subtract(quantia);
                    transacoes.add(new RegistroTransacao(quantia, TipoTransacao.DEBITO, LocalDateTime.now()));
                    System.out.println("Saque realizado com sucesso!");
                } else {
                    throw new SaldoInsuficienteException("Saldo insuficiente.");
                }
            } else {
                throw new ValorInvalidoException("Valor inválido para saque.");
            }
        } else {
            throw new ContaInexistenteException("Conta inativa.");
        }
    }

	// Método abstrato para transferir valores entre contas.
	public abstract void transferir(IConta destino, BigDecimal valor) throws ContaInexistenteException, SaldoInsuficienteException, ValorInvalidoException;
	
	// Imprime o extrato da conta bancária para um mês e ano específicos.
	@Override
    public void imprimirExtratoConta(int mes, int year) {
        System.out.println("Extrato da conta " + numeroConta + ":");
        System.out.println("Data\t\tTipo\t\tValor");
        for (RegistroTransacao transacao : transacoes) {
            if (transacao.getData().getMonthValue() == mes && transacao.getData().getYear() == year) {
                System.out.printf("%s\t%s\t%.2f%n", 
                                  transacao.getData(), 
                                  transacao.getTipo(), 
                                  transacao.getValor().doubleValue());
            }
        }
    }

	@Override
	public String toString() {
		return "ContaBancaria [numeroConta=" + numeroConta + ", saldo=" + saldo + ", dataAbertura=" + dataAbertura
				+ ", status=" + status + "]";
	}
	
}
