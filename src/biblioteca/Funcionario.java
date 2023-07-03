package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.FuncionarioDAO;
import session.Session;

public class Funcionario extends Usuario implements AcessoSistema, BooleanDisplayPattern {
    // ATRIBUTOS
    protected String cargo;
    protected boolean admin;

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
    public void login(String cpf, String senha) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();
        Funcionario funcionario = dao.getFuncionario(cpf);
        dao.closeConnection();

        if (funcionario == null || !senha.equals(funcionario.senha)) {
            throw new Exception("CPF ou senha incorretos.");
        }

        this.cpf = funcionario.cpf;
        this.senha = funcionario.senha;
        this.nome = funcionario.nome;
        this.dataNasc = funcionario.dataNasc;
        this.cargo = funcionario.cargo;
        this.admin = funcionario.admin;

        if (this.admin) {
            Session.login(Admin.parseAdmin(this));
        } else {
            Session.login(this);
        }

        System.out.println("Login realizado com sucesso!");
    }

    public void logout() {
        Session.logout();
    }

    public void alterarSenha(String senhaAtual, String novaSenha) throws Exception {
        if (!senhaAtual.equals(this.senha)) {
            throw new Exception("Senha atual incorreta.");
        }

        this.senha = novaSenha;

        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            dao.update(this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Funcionario> getListaFuncionarios(int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getListaFuncionarios(limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return null;
        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Funcionario> getFuncionariosByCpfLike(String cpf, int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getFuncionariosByCpfLike(cpf, limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return new ArrayList<Funcionario>();
        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Funcionario> getFuncionariosByNomeLike(String nome, int limit, int offset) {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            return dao.getFuncionariosByNomeLike(nome, limit, offset);
        } catch (Exception e) {
            System.out.println("Erro ao buscar lista de funcionários: " + e);

            return new ArrayList<Funcionario>();
        } finally {
            dao.closeConnection();
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
