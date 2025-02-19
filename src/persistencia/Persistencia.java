package persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.IConta;

import exception.PersistenciaException;

public class Persistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Cliente> cadastroClientes = new ArrayList<>();
    private static Persistencia instance;

    private Persistencia() {
        carregarDadosDeArquivo();
    }

    public static Persistencia getInstance() {
        if (instance == null) {
            instance = new Persistencia();
        }
        return instance;
    }

    // Salva um cliente no cadastro, evitando duplicação
    public void salvarCliente(Cliente c) throws PersistenciaException {
        if (!cadastroClientes.contains(c)) {
            cadastroClientes.add(c);
            salvarDadosEmArquivo();
            System.out.println("Cliente cadastrado com sucesso! CPF: " + c.getCpf());
        } else {
            System.err.println("Cliente já cadastrado no sistema! CPF: " + c.getCpf());
        }
    }

    // Remove um cliente do cadastro
    public void removerCliente(Cliente c) throws PersistenciaException {
        if (cadastroClientes.remove(c)) {
            salvarDadosEmArquivo();
            System.out.println("Cliente removido com sucesso! CPF: " + c.getCpf());
        } else {
            System.err.println("Cliente não encontrado no sistema! CPF: " + c.getCpf());
        }
    }

    // Localiza um cliente pelo CPF
    public Cliente localizarClientePorCPF(String cpf) {
        return cadastroClientes.stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    // Atualiza os dados de um cliente no cadastro
    public void atualizarClienteCadastro(Cliente c) throws PersistenciaException {
        int index = cadastroClientes.indexOf(c);
        if (index != -1) {
            Cliente clienteAntigo = cadastroClientes.get(index);
            cadastroClientes.set(index, c);
            salvarDadosEmArquivo();
            System.out.println("Cliente atualizado com sucesso. CPF: " + c.getCpf() + ". Alteração: " + 
                               (c.equals(clienteAntigo) ? "Nenhuma alteração detectada" : "Dados alterados"));
        } else {
            throw new IllegalArgumentException("Cliente não encontrado no cadastro. CPF: " + c.getCpf());
        }
    }

    // Localiza uma conta pelo número
    public IConta localizarContaPorNumero(int numeroConta) {
        return cadastroClientes.stream()
                .flatMap(cliente -> cliente.getContas().stream())
                .filter(conta -> conta.getNumeroConta() == numeroConta)
                .findFirst()
                .orElse(null);
    }
    
    // Localiza um cliente a partir de uma conta
    public Cliente localizarClientePorConta(IConta conta) {
        return cadastroClientes.stream()
                .filter(cliente -> cliente.getContas().contains(conta))
                .findFirst()
                .orElse(null);
    }

    // Salva os dados da lista de clientes em um arquivo
    public void salvarDadosEmArquivo() throws PersistenciaException {
        try (FileOutputStream fos = new FileOutputStream("dados");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(cadastroClientes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados no arquivo: " + e.getMessage());
            throw new PersistenciaException("Erro ao salvar os dados no arquivo", e);
        }
    }

    // Carrega os dados do arquivo
    @SuppressWarnings("unchecked")
    private void carregarDadosDeArquivo() {
        try (FileInputStream fis = new FileInputStream("dados");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            cadastroClientes = (ArrayList<Cliente>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Iniciando com lista vazia.");
            cadastroClientes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do arquivo: " + e.getMessage());
            cadastroClientes = new ArrayList<>(); // Fallback to empty list on failure
        }
    }
    
    // Lista todos os clientes cadastrados no sistema
    public void listarClientes() {
        if (cadastroClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            cadastroClientes.forEach(cliente -> 
                System.out.println("CPF: " + cliente.getCpf() + " - Nome: " + cliente.getNome())
            );
        }
    }
}