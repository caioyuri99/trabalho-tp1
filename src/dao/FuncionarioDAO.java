package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
    public void insert(Funcionario funcionario) throws Exception {
        String query = "INSERT INTO funcionario(cpf, senha, nome, dataNasc, cargo, admin) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getSenha());
            stmt.setString(3, funcionario.getNome());
            stmt.setDate(4, Date.valueOf(funcionario.getDataNasc()));
            stmt.setString(5, funcionario.getCargo());
            stmt.setBoolean(6, funcionario.isAdmin());
            stmt.execute();

            System.out.println("Funcionário inserido com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao inserir funcionário: " + e);
        }
    }

    public void update(Funcionario funcionario) throws Exception {
        String query = "UPDATE funcionario SET senha = ?, nome = ?, dataNasc = ?, cargo = ?, admin = ? WHERE cpf = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcionario.getSenha());
            stmt.setString(2, funcionario.getNome());
            stmt.setDate(3, Date.valueOf(funcionario.getDataNasc()));
            stmt.setString(4, funcionario.getCargo());
            stmt.setBoolean(5, funcionario.isAdmin());
            stmt.setString(6, funcionario.getCpf());
            stmt.execute();

            System.out.println("Funcionário atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar funcionário: " + e);
        }
    }

    public void delete(Funcionario funcionario) throws Exception {
        String query = "DELETE FROM funcionario WHERE cpf = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcionario.getCpf());
            stmt.execute();

            System.out.println("Funcionário deletado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao deletar funcionário: " + e);
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
            funcionario.setAdmin(rs.getBoolean("admin"));

            return funcionario;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e);

            return null;
        }
    }

    public ArrayList<Funcionario> getListaFuncionarios(int limit, int offset) throws Exception {
        String query = "SELECT * FROM funcionario LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();

                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSenha(rs.getString("senha"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                funcionario.setCargo(rs.getString("cargo"));
                funcionario.setAdmin(rs.getBoolean("admin"));

                listaFuncionarios.add(funcionario);
            }

            return listaFuncionarios;

        } catch (Exception e) {
            throw new Exception("Erro ao listar funcionários: " + e);
        }
    }

    public ArrayList<Funcionario> getFuncionariosByCpfLike(String cpf, int limit, int offset) throws Exception {
        String query = "SELECT * FROM funcionario WHERE cpf LIKE ? LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, "%" + cpf + "%");
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();

                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSenha(rs.getString("senha"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                funcionario.setCargo(rs.getString("cargo"));
                funcionario.setAdmin(rs.getBoolean("admin"));

                listaFuncionarios.add(funcionario);
            }

            return listaFuncionarios;

        } catch (Exception e) {
            throw new Exception("Erro ao listar funcionários: " + e);
        }
    }

    public ArrayList<Funcionario> getFuncionariosByNomeLike(String nome, int limit, int offset) throws Exception {
        String query = "SELECT * FROM funcionario WHERE nome LIKE ? LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, "%" + nome + "%");
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();

                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSenha(rs.getString("senha"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                funcionario.setCargo(rs.getString("cargo"));
                funcionario.setAdmin(rs.getBoolean("admin"));

                listaFuncionarios.add(funcionario);
            }

            return listaFuncionarios;

        } catch (Exception e) {
            throw new Exception("Erro ao listar funcionários: " + e);
        }
    }

}
