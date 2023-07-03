package biblioteca;

import java.time.LocalDate;

import dao.ClienteDAO;
import dao.FuncionarioDAO;

public class Admin extends Funcionario {
    // CONSTRUTORES
    public Admin() {
    }

    public Admin(String cpf, String senha, String nome, LocalDate dataNasc, String cargo, boolean admin) {
        super(cpf, senha, nome, dataNasc, cargo, admin);
    }

    // MÉTODOS
    public void fazerCadastro(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        if (dao.getFuncionario(funcionario.getCpf()) != null) {
            throw new Exception("CPF já cadastrado.");
        }

        try {
            dao.insert(funcionario);
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void fazerCadastro(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        if (dao.getCliente(cliente.getCpf()) != null) {
            throw new Exception("CPF já cadastrado.");
        }

        try {
            dao.insert(cliente);
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void deletarUsuario(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            dao.delete(funcionario);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void deletarUsuario(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        try {
            dao.delete(cliente);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void editarUsuario(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        try {
            dao.update(funcionario);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void editarUsuario(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        try {
            dao.update(cliente);

        } catch (Exception e) {
            throw new Exception(e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public static Admin parseAdmin(Funcionario funcionario) {
        return new Admin(funcionario.cpf, funcionario.senha, funcionario.nome, funcionario.dataNasc, funcionario.cargo,
                funcionario.admin);
    }
}
