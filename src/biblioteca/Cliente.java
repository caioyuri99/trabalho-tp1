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

    public void pagarMulta(double valor) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        Cliente cliente = dao.getCliente(cpf);

        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        this.saldoDevedor -= valor;

        dao.update(cpf, senha, nome, dataNasc, saldoDevedor);
    }

    public void fazerEmprestimo() throws Exception {
        if (this.saldoDevedor > 0) {
            throw new Exception("O cliente possui multa.");
        }

        if (this.carrinho.size() == 0) {
            throw new Exception("O carrinho está vazio.");
        }

        EmprestimoDAO dao = new EmprestimoDAO();

        for (Emprestimo emprestimo : this.getEmprestimosAtivos()) {
            if (!emprestimo.isDevolvido() && emprestimo.isAtrasado()) {
                throw new Exception("O cliente possui empréstimos atrasados.");
            }
        }

        for (Item item : this.carrinho) {
            dao.insert(this, item);
            item.emprestar();
        }

        this.carrinho.clear();

        System.out.println("Emprestimo realizado com sucesso!");
    }

    public void fazerRenovacao(Emprestimo emprestimo) throws Exception {
        if (!this.cpf.equals(emprestimo.getLeitor().getCpf())) {
            throw new Exception("O empréstimo não pertence a esse cliente.");
        }

        if (emprestimo.getQtdRenovacoes() == 3) {
            throw new Exception("O empréstimo já foi renovado 3 vezes.");
        }

        if (this.saldoDevedor > 0) {
            throw new Exception("O cliente possui multa.");
        }

        if (emprestimo.isAtrasado()) {
            throw new Exception("O empréstimo está atrasado.");
        }

        EmprestimoDAO dao = new EmprestimoDAO();
        dao.renovacao(emprestimo.getId());

        Emprestimo updatedEmprestimo = dao.getEmprestimo(emprestimo.getId());
        emprestimo.setQtdRenovacoes(updatedEmprestimo.getQtdRenovacoes());
        emprestimo.setDataDevolucao(updatedEmprestimo.getDataDevolucao());
    }

    public void fazerDevolucao(Emprestimo emprestimo) throws Exception {
        EmprestimoDAO dao = new EmprestimoDAO();
        dao.devolucao(emprestimo.getId());
        emprestimo.getItem().devolver();
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

    public ArrayList<Emprestimo> getEmprestimosAtivos() {
        EmprestimoDAO dao = new EmprestimoDAO();

        try {
            return dao.getEmprestimosAtivos(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    public void atualizarCadastro() throws Exception {
        ClienteDAO dao = new ClienteDAO();

        dao.update(this.cpf, this.senha, this.nome, this.dataNasc, this.saldoDevedor);
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
