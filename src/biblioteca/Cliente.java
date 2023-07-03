package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.ClienteDAO;
import dao.EmprestimoDAO;
import exceptions.Confirmation;
import session.Session;

public class Cliente extends Usuario implements AcessoSistema {
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
    public void login(String cpf, String senha) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        Cliente cliente;
        try {
            cliente = dao.getCliente(cpf);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }

        if (cliente == null || !senha.equals(cliente.senha)) {
            throw new Exception("CPF ou senha incorretos.");
        }

        this.cpf = cliente.cpf;
        this.senha = cliente.senha;
        this.nome = cliente.nome;
        this.dataNasc = cliente.dataNasc;
        this.saldoDevedor = cliente.saldoDevedor;

        Session.login(this);

        System.out.println("Login realizado com sucesso!");
    }

    public void logout() {
        Session.logout();
    }

    public void fazerCadastro() throws Exception {
        ClienteDAO dao = new ClienteDAO();

        if (dao.getCliente(this.cpf) != null) {
            throw new Exception("CPF já cadastrado.");
        }

        try {
            dao.insert(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void alterarSenha(String senhaAtual, String novaSenha) throws Exception {
        if (!senhaAtual.equals(this.senha)) {
            throw new Exception("Senha atual incorreta.");
        }

        this.senha = novaSenha;

        ClienteDAO dao = new ClienteDAO();

        try {
            dao.update(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void delete() throws Exception {
        ClienteDAO dao = new ClienteDAO();

        try {
            dao.delete(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void calcularMulta() throws Exception {
        EmprestimoDAO dao = new EmprestimoDAO();
        this.atualizaAtrasosMultas();

        try {
            this.saldoDevedor = dao.getTotalMultas(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    private void atualizaAtrasosMultas() throws Exception {
        EmprestimoDAO dao = new EmprestimoDAO();

        try {
            dao.atualizaAtrasosMultas(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void pagarMulta(Emprestimo emprestimo) throws Exception {
        ClienteDAO clienteDAO = new ClienteDAO();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

        Cliente cliente = clienteDAO.getCliente(this.cpf);

        if (cliente == null) {
            throw new Exception("Cliente não encontrado.");
        }

        if (!emprestimo.isMultado() || emprestimo.isPago()) {
            throw new Exception("O empréstimo não possui multa.");
        }

        try {
            emprestimoDAO.pagarMulta(cliente, emprestimo);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            clienteDAO.closeConnection();
            emprestimoDAO.closeConnection();
        }

        this.calcularMulta();
    }

    public void fazerEmprestimo() throws Exception {
        if (this.saldoDevedor > 0) {
            throw new Exception("O cliente possui multa.");
        }

        if (this.carrinho.size() == 0) {
            throw new Exception("O carrinho está vazio.");
        }

        for (Emprestimo emprestimo : this.getEmprestimosAtivos()) {
            if (!emprestimo.isDevolvido() && emprestimo.isAtrasado()) {
                throw new Exception("O cliente possui empréstimos atrasados.");
            }
        }

        for (Item item : this.carrinho) {
            EmprestimoDAO dao = new EmprestimoDAO();

            try {
                dao.insert(this, item);

            } catch (Exception e) {
                throw new Exception(e.getMessage());

            } finally {
                dao.closeConnection();
            }

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

        try {
            EmprestimoDAO dao = new EmprestimoDAO();
            dao.renovacao(emprestimo.getId());
            dao.closeConnection();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        EmprestimoDAO dao = new EmprestimoDAO();
        Emprestimo updatedEmprestimo = dao.getEmprestimo(emprestimo.getId());
        emprestimo.setQtdRenovacoes(updatedEmprestimo.getQtdRenovacoes());
        emprestimo.setDataDevolucao(updatedEmprestimo.getDataDevolucao());
        dao.closeConnection();
    }

    public void fazerDevolucao(Emprestimo emprestimo) throws Exception {
        EmprestimoDAO dao = new EmprestimoDAO();
        try {
            dao.devolucao(emprestimo.getId());
            emprestimo.getItem().devolver();

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public int getTotalEmprestimos() {
        EmprestimoDAO dao = new EmprestimoDAO();

        int totalEmprestimos = dao.getTotalEmprestimos(this);

        dao.closeConnection();

        return totalEmprestimos;
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
        } finally {
            dao.closeConnection();
        }
    }

    public ArrayList<Emprestimo> getHistoricoEmprestimos(int limit, int offset) {
        EmprestimoDAO dao = new EmprestimoDAO();

        try {
            return dao.getHistoricoEmprestimos(this, limit, offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        } finally {
            dao.closeConnection();
        }
    }

    public ArrayList<Emprestimo> filtraHistoricoEmprestimos(String search, LocalDate dateFrom, LocalDate dateTo,
            int limit, int offset) {
        EmprestimoDAO dao = new EmprestimoDAO();

        try {
            return dao.filtraHistoricoEmprestimos(this, search, dateFrom, dateTo, limit, offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ArrayList<Emprestimo>();
        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Cliente> getClientesByCpfLike(String cpf, int limit, int offset) {
        ClienteDAO dao = new ClienteDAO();

        try {
            return dao.getClientesByCpfLike(cpf, limit, offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ArrayList<Cliente>();
        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Cliente> getClientesByNomeLike(String nome, int limit, int offset) {
        ClienteDAO dao = new ClienteDAO();

        try {
            return dao.getClientesByNomeLike(nome, limit, offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ArrayList<Cliente>();
        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Cliente> getListaClientes(int limit, int offset) {
        ClienteDAO dao = new ClienteDAO();

        try {
            return dao.getListaClientes(limit, offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        } finally {
            dao.closeConnection();
        }
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
