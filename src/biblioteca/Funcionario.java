package biblioteca;

import java.time.LocalDate;

import dao.ClienteDAO;
import dao.FuncionarioDAO;

public class Funcionario extends Usuario {
    // ATRIBUTOS
    private String cargo;

    // CONSTRUTORES
    public Funcionario() {
    }

    public Funcionario(String nome, String cpf, String senha, LocalDate dataNasc, String cargo) {
        super(nome, cpf, senha, dataNasc);
        this.cargo = cargo;
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

        System.out.println("Login realizado com sucesso!");

        return true;
    }

    public boolean fazerCadastro(Cliente cliente) {
        ClienteDAO dao = new ClienteDAO();

        if (dao.getCliente(cliente.cpf) != null) {
            System.out.println("Um cliente com esse CPF já foi cadastrado.");

            return false;
        }

        boolean res = dao.insert(cliente);

        return res;
    }

    // GETTERS & SETTERS
    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}
