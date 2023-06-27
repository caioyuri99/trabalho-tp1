package biblioteca;

import java.time.LocalDate;

import dao.ClienteDAO;
import dao.FuncionarioDAO;

public class Admin extends Funcionario {
    // CONSTRUTORES
    public Admin() {
    }

    public Admin(String cpf, String senha, String nome, LocalDate dataNasc, String cargo) {
        super(cpf, senha, nome, dataNasc, cargo);
    }

    // MÉTODOS
    public void fazerCadastro(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        if (dao.getFuncionario(funcionario.getCpf()) != null) {
            throw new Exception("CPF já cadastrado.");
        }

        dao.insert(funcionario);
    }

    public void fazerCadastro(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        if (dao.getCliente(cliente.getCpf()) != null) {
            throw new Exception("CPF já cadastrado.");
        }

        dao.insert(cliente);
    }

    public void deletarUsuario(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        dao.delete(funcionario);
    }

    public void deletarUsuario(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        dao.delete(cliente);
    }

    public void editarUsuario(Funcionario funcionario) throws Exception {
        FuncionarioDAO dao = new FuncionarioDAO();

        dao.update(funcionario);
    }

    public void editarUsuario(Cliente cliente) throws Exception {
        ClienteDAO dao = new ClienteDAO();

        dao.update(cliente);
    }
}
