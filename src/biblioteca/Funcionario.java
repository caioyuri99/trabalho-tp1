package biblioteca;

import java.time.LocalDate;

import dao.FuncionarioDAO;

public class Funcionario extends Usuario {
    // ATRIBUTOS
    private String cargo;

    // CONSTRUTORES
    public Funcionario() {
    }

    public Funcionario(String cpf, String senha, String nome, LocalDate dataNasc, String cargo) {
        super(cpf, senha, nome, dataNasc);
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

    // GETTERS & SETTERS
    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}
