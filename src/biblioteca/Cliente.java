package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.ClienteDAO;
import dao.EmprestimoDAO;
import exceptions.Confirmation;

public class Cliente extends Usuario {
    // ATRIBUTOS
    private double saldoDevedor;
    private ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
    private ArrayList<Item> carrinho = new ArrayList<Item>();

    // CONSTRUTORES
    public Cliente() {
    }

    public Cliente(String cpf, String senha, String nome, LocalDate dataNasc) {
        super(cpf, senha, nome, dataNasc);
    }

    // METODOS
    @Override
    public boolean fazerLogin(String cpf, String senha) {
        ClienteDAO dao = new ClienteDAO();
        Cliente cliente = dao.getCliente(cpf);

        if (cliente == null || !senha.equals(cliente.senha)) {
            System.out.println("CPF ou senha incorretos.");

            return false;
        }

        this.cpf = cliente.cpf;
        this.senha = cliente.senha;
        this.nome = cliente.nome;
        this.dataNasc = cliente.dataNasc;
        this.saldoDevedor = cliente.saldoDevedor;

        System.out.println("Login realizado com sucesso!");

        return true;
    }

    public boolean fazerCadastro() {
        if (this.cpf == null || this.senha == null || this.nome == null || this.dataNasc == null) {
            System.out.println("Preencha todos os campos.");

            return false;
        }

        ClienteDAO dao = new ClienteDAO();

        if (dao.getCliente(this.cpf) != null) {
            System.out.println("Um cliente com esse CPF já foi cadastrado.");

            return false;
        }

        boolean res = dao.insert(this);

        return res;
    }

    public boolean pagarMulta(double valor) {
        ClienteDAO dao = new ClienteDAO();

        Cliente cliente = dao.getCliente(cpf);

        if (cliente == null) {
            System.out.println("O cliente não está cadastrado.");

            return false;
        }

        this.saldoDevedor -= valor;

        boolean res = dao.update(cpf, senha, nome, dataNasc, saldoDevedor);

        if (!res) {
            System.out.println("Erro ao pagar multa.");

            return false;
        }

        System.out.println("Multa paga com sucesso!");

        return true;
    }

    public boolean fazerEmprestimo(Emprestimo emprestimo) {
        if (this.getTotalEmprestimos() + emprestimo.getItems().size() > 5) {
            System.out.println("Somente 5 itens podem ser emprestados por vez.");

            return false;
        }

        if (this.saldoDevedor > 0) {
            System.out.println("O cliente possui multa.");

            return false;
        }

        for (Emprestimo emp : this.emprestimos) {
            if (emp.getDataDevolucao().isBefore(LocalDate.now())) {
                System.out.println("O cliente possui itens atrasados.");

                return false;
            }
        }

        EmprestimoDAO dao = new EmprestimoDAO();
        for (Item item : emprestimo.getItems()) {
            boolean registrar = dao.insert(this, item);

            boolean emprestar = item.emprestar(this);

            if (!registrar || !emprestar) {
                System.out.println("Erro ao fazer emprestimo.");

                return false;
            }
        }

        return true;
    }

    public boolean fazerRenovacao(Emprestimo emprestimo) {
        if (!this.cpf.equals(emprestimo.getLeitor().getCpf())) {
            System.out.println("O emprestimo não pertence ao cliente.");

            return false;
        }

        if (emprestimo.getQtdRenovacoes() == 3) {
            System.out.println("O emprestimo já foi renovado 3 vezes.");

            return false;
        }

        if (this.saldoDevedor > 0) {
            System.out.println("O cliente possui multa.");

            return false;
        }

        EmprestimoDAO dao = new EmprestimoDAO();
        boolean res = dao.renovacao(emprestimo.getId());

        if (!res) {
            System.out.println("Erro ao fazer renovação.");

            return false;
        }

        emprestimo.setQtdRenovacoes(emprestimo.getQtdRenovacoes() + 1);

        return true;
    }

    public boolean fazerDevolucao(Emprestimo emprestimo) {
        if (!this.cpf.equals(emprestimo.getLeitor().getCpf())) {
            System.out.println("O emprestimo não pertence ao cliente.");

            return false;
        }

        EmprestimoDAO dao = new EmprestimoDAO();

        boolean res = dao.devolucao(emprestimo.getId());

        if (!res) {
            System.out.println("Erro ao fazer devolução.");

            return false;
        }

        return true;
    }

    public int getTotalEmprestimos() {
        EmprestimoDAO dao = new EmprestimoDAO();

        return dao.getTotalEmprestimos(this);
    }

    public void adicionarAoCarrinho(Item item) throws Exception {
        int total = this.getTotalEmprestimos();

        if (total + this.carrinho.size() == 5) {
            if (total > 0) {
                throw new Exception(
                        "O limite de empréstimos (5) foi atingido. Devolva algum item para poder emprestar mais.");
            }
            throw new Exception("O limite de empréstimos (5) foi atingido.");
        }

        if (this.carrinho.contains(item)) {
            throw new Exception("Esse item já foi adicionado ao seu carrinho.");
        }

        for (Item i : this.carrinho) {
            if (i.getObra().getId() == item.getObra().getId()) {
                throw new Confirmation("Um item dessa obra já foi adicionado ao carrinho. Deseja adicionar mais um?");
            }
        }

        this.carrinho.add(item);
    }

    // GETTERS & SETTERS
    public double getSaldoDevedor() {
        return this.saldoDevedor;
    }

    public void setSaldoDevedor(double valor) {
        this.saldoDevedor = valor;
    }

    public ArrayList<Emprestimo> getEmprestimos() {
        return this.emprestimos;
    }

    public void setEmprestimos(ArrayList<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public ArrayList<Item> getCarrinho() {
        return this.carrinho;
    }

    public void setCarrinho(ArrayList<Item> carrinho) {
        this.carrinho = carrinho;
    }

}
