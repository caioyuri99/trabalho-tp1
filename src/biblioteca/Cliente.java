package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.ClienteDAO;
import dao.EmprestimoDAO;
import dao.GibiDAO;
import dao.LivroDAO;
import dao.RevistaDAO;

public class Cliente extends Usuario {
    // ATRIBUTOS
    private double saldoDevedor;
    private ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();

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

        // TODO: Verificar se o cliente tem multa
        // TODO: Verificar se o cliente tem algum item atrasado
        // TODO: Verificar se o item está disponível

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

    public boolean fazerRenovacao(Item item) {
        return false;
    }

    public boolean fazerDevolucao(Item item) {
        return false;
    }

    public int getTotalEmprestimos() {
        int tot = 0;

        // FIXME: SOLUÇÃO TEMPORÁRIA PARA CONTAR EMPRESTIMOS
        LivroDAO livroDAO = new LivroDAO();
        RevistaDAO revistaDAO = new RevistaDAO();
        GibiDAO gibiDAO = new GibiDAO();

        tot += livroDAO.getEmprestados(this).size();
        tot += revistaDAO.getEmprestados(this).size();
        tot += gibiDAO.getEmprestados(this).size();

        return tot;
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

}
