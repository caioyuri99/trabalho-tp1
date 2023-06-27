package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.FuncionarioDAO;

public class Funcionario extends Usuario {
    // ATRIBUTOS
    private String cargo;
    private boolean admin;

    // CONSTRUTORES
    public Funcionario() {
    }

    public Funcionario(String cpf, String senha, String nome, LocalDate dataNasc, String cargo) {
        super(cpf, senha, nome, dataNasc);
        this.cargo = cargo;
    }

    public Funcionario(String cpf, String senha, String nome, LocalDate dataNasc, String cargo, boolean admin) {
        super(cpf, senha, nome, dataNasc);
        this.cargo = cargo;
        this.admin = admin;
    }

    // METODOS
    @Override
    public boolean fazerLogin(String cpf, String senha) {
        FuncionarioDAO dao = new FuncionarioDAO();
        Funcionario funcionario = dao.getFuncionario(cpf);

        if (funcionario == null || !senha.equals(funcionario.senha)) {
            System.out.println("CPF ou senha incorretos.");

            return false;
        }

        this.cpf = funcionario.cpf;
        this.senha = funcionario.senha;
        this.nome = funcionario.nome;
        this.dataNasc = funcionario.dataNasc;
        this.cargo = funcionario.cargo;
        this.admin = funcionario.admin;

        System.out.println("Login realizado com sucesso!");

        return true;
    }

    public static ArrayList<Funcionario> getListaFuncionarios(int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getListaFuncionarios(limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return null;
        }
    }

    public static ArrayList<Funcionario> getFuncionariosByCpfLike(String cpf, int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getFuncionariosByCpfLike(cpf, limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return new ArrayList<Funcionario>();
        }
    }

    public static ArrayList<Funcionario> getFuncionariosByNomeLike(String nome, int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getFuncionariosByNomeLike(nome, limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return new ArrayList<Funcionario>();
        }
    }

    // GETTERS & SETTERS
    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
