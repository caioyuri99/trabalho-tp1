package biblioteca;

import java.time.LocalDate;

import dao.FuncionarioDAO;

public class Admin extends Funcionario {
    // CONSTRUTORES
    public Admin() {
    }

    public Admin(String cpf, String senha, String nome, LocalDate dataNasc, String cargo) {
        super(cpf, senha, nome, dataNasc, cargo);
    }

    // MÉTODOS
    public boolean fazerCadastro(Funcionario funcionario) {
        FuncionarioDAO dao = new FuncionarioDAO();

        if (!dao.insert(funcionario)) {
            System.out.println("Erro ao cadastrar funcionário.");

            return false;
        }

        return true;
    }
}
