package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import biblioteca.Funcionario;
import connection.ConnectionDB;

public class FuncionarioDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public FuncionarioDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public boolean insert(Funcionario funcionario) {
        String query = "INSERT INTO funcionario(cpf, senha, nome, dataNasc, cargo) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getSenha());
            stmt.setString(3, funcionario.getNome());
            stmt.setDate(4, Date.valueOf(funcionario.getDataNasc()));
            stmt.setString(5, funcionario.getCargo());
            stmt.execute();

            System.out.println("Funcionário inserido com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());

            return false;
        }
    }

    public Funcionario getFuncionario(String cpf) {
        String query = "SELECT * FROM funcionario WHERE cpf = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            Funcionario funcionario = new Funcionario();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return null;
            }

            funcionario.setCpf(rs.getString("cpf"));
            funcionario.setSenha(rs.getString("senha"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setDataNasc(rs.getDate("dataNasc").toLocalDate());
            funcionario.setCargo(rs.getString("cargo"));

            return funcionario;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e);

            return null;
        }
    }
}
