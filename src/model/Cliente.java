package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import persistencia.Persistencia;

public class Cliente implements Serializable{
	private static final long serialVersionUID = 1L;

	private String cpf;
	private String nome;
	
	
	private List<IConta> contas;
	
	public Cliente() {
		
	}

	public Cliente(String cpf, String nome) {
		this.cpf = cpf;
		this.nome = nome;
		contas = new ArrayList<>();
	}

	// Getters e Setters
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<IConta> getContas() {
		return contas;
	}

	public void setContas(List<IConta> contas) {
		this.contas = contas;
	}


	@Override
	public String toString() {
		return "Cliente [cpf=" + cpf + ", nome=" + nome + ", contas=" + contas + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(cpf, other.cpf);
	}
	
	// Adiciona uma conta ao cliente.
	public void adicionarConta(IConta conta) {
	    if (contas.contains(conta)) {
	        System.out.println("A conta já está associada a este cliente.");
	    } else {
	        this.contas.add(conta);
	        Persistencia.getInstance().salvarDadosEmArquivo();
	        System.out.println("Conta adicionada com sucesso!");
	    }
	}

	// Adiciona uma conta do tipo ContaCorrente ao cliente.
    public void adicionarContaa(ContaCorrente conta1) {
    	if (contas.contains(conta1)) {
    		System.out.println("A conta já está associada a este cliente.");
    	} else {
    		this.contas.add((IConta) conta1);
    		Persistencia.getInstance().salvarDadosEmArquivo();
    		System.out.println("Conta adicionada com sucesso!");
    	}
    }

    // Remove uma conta do cliente.
    public void removerConta(IConta c) {
    	if(contas.contains(c)) {
    		this.contas.remove(c);
    		Persistencia.getInstance().salvarDadosEmArquivo();
    		System.out.println("Conta removida com sucesso!");
    	} else {
    		System.out.println("A conta não está associada a este cliente.");
    	}
    }

    // Localiza uma conta pelo número.
    public IConta localizarContaNumero(int numero) {
		for (int i = 0; i < contas.size(); i++) {
			IConta c = contas.get(i);
			if (c.getNumeroConta() == numero) {
				System.out.println("Conta encontrada!");
				return c;
			}
		}
		System.out.println("Conta não encontrada.");
		return null;
    }

    // Retorna o saldo total entre todas as contas do cliente.
    public double balancoEntreContas() {
		double valorSaldo = 0.0;
		for (int i = 0; i < contas.size(); i++) {
			IConta c = contas.get(i);
			valorSaldo += c.getSaldo().doubleValue();
		}
		System.out.println("Balanço entre contas: R$" + valorSaldo);
		return valorSaldo;
    }
}