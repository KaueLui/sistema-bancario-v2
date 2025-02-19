package app;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.InputMismatchException;

import model.Cliente;
import model.ContaCorrente;
import model.ContaPoupanca;
import model.IConta;
import model.RegistroTransacao;
import persistencia.Persistencia;

import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;

public class Programa {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Cliente cliente = null;

        System.out.println("[Sistema Bancário]");
        // Loop infinito para manter o programa rodando
        while (true) {
            System.out.println("1. Cadastrar novo cliente");
            System.out.println("2. Selecionar cliente existente");
            System.out.println("3. Listar clientes cadastrados");
            System.out.println("4. Consultar cliente por CPF");
            System.out.println("5. Remover Cliente");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    try {
                        System.out.println("Digite o nome do cliente:");
                        String nome = scanner.nextLine();
                        System.out.println("Digite o CPF do cliente:");
                        String cpf = scanner.nextLine();
                        cliente = new Cliente(cpf, nome);
                        Persistencia.getInstance().salvarCliente(cliente);
                    } catch (Exception e) {
                        System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Insira o CPF do cliente:");
                        String cpf = scanner.next();
                        cliente = Persistencia.getInstance().localizarClientePorCPF(cpf);

                        if (cliente == null) {
                            System.out.println("Cliente não encontrado");
                            break;
                        }

                        System.out.println("Cliente selecionado: " + cliente.getNome());
                        while (true) {
                            System.out.println("1. Criar nova conta");
                            System.out.println("2. Listar contas do cliente");
                            System.out.println("3. Realizar Depósito");
                            System.out.println("4. Realizar Saque");
                            System.out.println("5. Realizar Transferência");
                            System.out.println("6. Imprimir extrato da movimentação");
                            System.out.println("7. Consultar saldo da conta");
                            System.out.println("8. Consultar balanço entre contas");
                            System.out.println("9. Remover conta");
                            System.out.println("0. Voltar ao menu principal");

                            int opcaoConta = scanner.nextInt();
                            scanner.nextLine();

                            if (opcaoConta == 0) break;

                            switch (opcaoConta) {
                                case 1:
                                    try {
                                        System.out.println("Escolha o tipo de conta que deseja criar");
                                        System.out.println("1. Conta Poupanca");
                                        System.out.println("2. Conta Corrente");

                                        int tipoConta = scanner.nextInt();
                                        int numeroConta = new Random().nextInt(100);
                                        scanner.nextLine();
                                    
                                        IConta novaConta;
                                        if (tipoConta == 1) {
                                            novaConta = new ContaPoupanca(numeroConta);
                                        } else if (tipoConta == 2) {
                                            novaConta = new ContaCorrente(numeroConta);
                                        } else {
                                            throw new ValorInvalidoException("Tipo de conta inválido.");
                                        }

                                        cliente.adicionarConta(novaConta);
                                        Persistencia.getInstance().atualizarClienteCadastro(cliente);
                                        System.out.println("Conta criada com sucesso!");
                                    } catch (ValorInvalidoException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao criar conta: " + e.getMessage());
                                    }
                                    break;

                                case 2:
                                    for (IConta c : cliente.getContas()) {
                                        System.out.println(c);
                                    }
                                    break;

                                case 3:
                                    try {
                                        System.out.println("Selecione a conta para depósito:");
                                        for (IConta c : cliente.getContas()) {
                                            System.out.println(c);
                                        }
                                        int opcaoContaDeposito = scanner.nextInt();
                                        scanner.nextLine();
                                        System.out.println("Insira o valor da quantia a ser depositada: ");
                                        BigDecimal quantia = new BigDecimal(scanner.nextDouble());
                                        scanner.nextLine();
                                        IConta temp = cliente.localizarContaNumero(opcaoContaDeposito);
                                        if (temp != null) {
                                            temp.depositar(quantia);
                                            Persistencia.getInstance().atualizarClienteCadastro(cliente);
                                            System.out.println("Depósito realizado com sucesso.");
                                        } else {
                                            throw new ContaInexistenteException("Conta não encontrada.");
                                        }
                                    } catch (ContaInexistenteException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (ValorInvalidoException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao realizar depósito: " + e.getMessage());
                                    }
                                    break;

                                case 4:
                                    try {
                                        System.out.println("Selecione a conta para saque:");
                                        for (IConta c : cliente.getContas()) {
                                            System.out.println(c);
                                        }
                                        int opcaoContaSaque = scanner.nextInt();
                                        scanner.nextLine();
                                        System.out.println("Insira o valor:");
                                        BigDecimal quantia1 = new BigDecimal(scanner.nextDouble());
                                        scanner.nextLine();
                                        IConta temp1 = cliente.localizarContaNumero(opcaoContaSaque);
                                        if (temp1 != null) {
                                            temp1.sacar(quantia1);
                                            Persistencia.getInstance().atualizarClienteCadastro(cliente);
                                            System.out.println("Saque realizado com sucesso.");
                                        } else {
                                            throw new ContaInexistenteException("Conta não encontrada.");
                                        }
                                    } catch (ContaInexistenteException | SaldoInsuficienteException | ValorInvalidoException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao realizar saque: " + e.getMessage());
                                    }
                                    break;

                                case 5:
                                    try {
                                        if (cliente.getContas().size() > 0) {
                                            System.out.println("Contas do cliente atual:");
                                            for (IConta c : cliente.getContas()) {
                                                System.out.println(c);
                                            }

                                            System.out.println("Digite o número da conta de origem: ");
                                            int numeroContaOrigem = scanner.nextInt();
                                            IConta contaOrigem = cliente.localizarContaNumero(numeroContaOrigem);

                                            if (contaOrigem != null) {
                                                System.out.println("Digite o número da conta de destino: ");
                                                int numeroContaDestino = scanner.nextInt();
                                                IConta contaDestino = Persistencia.getInstance().localizarContaPorNumero(numeroContaDestino);

                                                if (contaDestino != null) {
                                                    System.out.println("Digite o valor da transferência: ");
                                                    BigDecimal quantia2 = new BigDecimal(scanner.nextDouble());
                                                    scanner.nextLine();

                                                    try {
                                                        contaOrigem.transferir(contaDestino, quantia2);
                                                                        
                                                        Persistencia.getInstance().atualizarClienteCadastro(cliente);
                                                         
                                                        if (!cliente.getContas().contains(contaDestino)) {
                                                            Cliente clienteDestino = Persistencia.getInstance().localizarClientePorConta(contaDestino);
                                                            if (clienteDestino != null) {
                                                                Persistencia.getInstance().atualizarClienteCadastro(clienteDestino);
                                                            } else {
                                                                throw new ContaInexistenteException("Não foi possível localizar o cliente da conta de destino.");
                                                            }
                                                        }
                                                        System.out.println("Transferência realizada com sucesso.");
                                                    } catch (Exception e) {
                                                        System.out.println("Erro ao atualizar a persistência: " + e.getMessage());
                                                    }
                                                } else {
                                                    throw new ContaInexistenteException("Conta de destino não encontrada.");
                                                }
                                            } else {
                                                throw new ContaInexistenteException("Conta de origem não encontrada.");
                                            }
                                        } else {
                                            throw new ContaInexistenteException("Nenhuma conta associada ao cliente.");
                                        }
                                    } catch (ContaInexistenteException | SaldoInsuficienteException | ValorInvalidoException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro desconhecido ao realizar a transferência: " + e.getMessage());
                                    }
                                    break;

                                case 6:
                                    try {
                                        if (cliente.getContas().size() > 0) {
                                            System.out.println("Selecione a conta para extrato:");
                                            for (IConta c : cliente.getContas()) {
                                                System.out.println(c);
                                            }
                                            System.out.println("Insira o número da conta:");
                                            int contaNumero = scanner.nextInt();
                                            IConta conta = cliente.localizarContaNumero(contaNumero);
                                            if (conta != null) {
                                                List<RegistroTransacao> transacoes = conta.getTransacoes();
                                                if (transacoes != null && !transacoes.isEmpty()) {
                                                    System.out.println("\nExtrato da conta número " + contaNumero + ":");
                                                    System.out.println("Data\t\t\tTipo\t\tValor");
                                                    System.out.println("--------------------------------------------");
                                                    for (RegistroTransacao transacao : transacoes) {
                                                        System.out.printf("%s\t%s\t%.2f%n", 
                                                                          transacao.getData(), 
                                                                          transacao.getTipo(), 
                                                                          transacao.getValor().doubleValue());
                                                    }
                                                    System.out.println("--------------------------------------------");
                                                } else {
                                                    System.out.println("Nenhuma transação encontrada para esta conta.");
                                                }
                                            } else {
                                                throw new ContaInexistenteException("Conta não encontrada.");
                                            }
                                        } else {
                                            throw new ContaInexistenteException("Nenhuma conta associada ao cliente.");
                                        }
                                    } catch (ContaInexistenteException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao imprimir extrato: " + e.getMessage());
                                    }
                                    break;

                                case 7:
                                    try {
                                        if (cliente.getContas().size() > 0) {
                                            System.out.println("Selecione a conta para consultar o saldo:");
                                            for (IConta conta : cliente.getContas()) {
                                                System.out.println("Conta Nº: " + conta.getNumeroConta());
                                            }

                                            System.out.print("Digite o número da conta: ");
                                            int numeroContaSaldo = scanner.nextInt();
                                            scanner.nextLine();
                                            IConta contaSaldo = cliente.localizarContaNumero(numeroContaSaldo);
                                            if (contaSaldo != null) {
                                                System.out.println("\nSaldo da conta Nº " + contaSaldo.getNumeroConta() + ": R$ " + contaSaldo.getSaldo());
                                            } else {
                                                throw new ContaInexistenteException("Conta não encontrada.");
                                            }
                                        } else {
                                            throw new ContaInexistenteException("Este cliente não possui contas cadastradas.");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                                        scanner.nextLine(); 
                                    } catch (ContaInexistenteException e) {
                                        System.out.println(e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao consultar saldo: " + e.getMessage());
                                    }
                                    break;

                                case 8:
                                    cliente.balancoEntreContas();
                                    break;

                                case 9:
                                    try {
                                        if (cliente.getContas().size() > 0) {
                                            System.out.println("Selecione a conta para remover:");
                                            for (IConta c : cliente.getContas()) {
                                                System.out.println(c);
                                            }

                                            System.out.println("Insira o número da conta:");
                                            int numeroConta1 = scanner.nextInt();
                                            IConta conta3 = cliente.localizarContaNumero(numeroConta1);
                                            if (conta3 != null) {
                                                // Check if the account balance is zero before allowing removal
                                                if (conta3.getSaldo().compareTo(BigDecimal.ZERO) == 0) {
                                                    cliente.removerConta(conta3);
                                                    Persistencia.getInstance().atualizarClienteCadastro(cliente);
                                                    System.out.println("Conta removida com sucesso.");
                                                } else {
                                                    System.out.println("Não é possível remover uma conta com saldo positivo.");
                                                }
                                            } else {
                                                throw new ContaInexistenteException("Conta não encontrada.");
                                            }
                                        } else {
                                            throw new ContaInexistenteException("Nenhuma conta associada ao cliente.");
                                        }
                                    } catch (ContaInexistenteException e) {
                                        System.out.println("Erro: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Erro ao remover conta: " + e.getMessage());
                                    }
                                    break;

                                default:
                                    System.out.println("Opção inválida");
                                }
                                }
                    			}
                        		catch (Exception e) {
                                    System.out.println("Erro ao selecionar cliente: " + e.getMessage());
                                }

                                break;

                case 3:
                    Persistencia.getInstance().listarClientes();
                    break;

                case 4:
                    try {
                        System.out.println("Insira o CPF do cliente:");
                        String cpf = scanner.next();
                        cliente = Persistencia.getInstance().localizarClientePorCPF(cpf);

                        if (cliente != null) {
                            System.out.println("Cliente encontrado:");
                            System.out.println("Nome: " + cliente.getNome());
                            System.out.println("CPF: " + cliente.getCpf());
                            System.out.println("\nContas vinculadas:");
                            for (IConta conta : cliente.getContas()) {
                                System.out.println(conta);
                            }
                        } else {
                            System.out.println("Cliente não encontrado.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao consultar cliente: " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.println("Insira o CPF do cliente:");
                        String cpf = scanner.next();
                        cliente = Persistencia.getInstance().localizarClientePorCPF(cpf);

                        if (cliente != null) {
                            Persistencia.getInstance().removerCliente(cliente);
                            System.out.println("Cliente removido com sucesso.");
                        } else {
                            System.out.println("Cliente não encontrado.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao remover cliente: " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}