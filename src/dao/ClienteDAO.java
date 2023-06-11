package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import biblioteca.Cliente;
import connection.ConnectionDB;

public class ClienteDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public ClienteDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public boolean insert(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, senha, nome, dataNasc) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getSenha());
            stmt.setString(3, cliente.getNome());
            stmt.setDate(4, Date.valueOf(cliente.getDataNasc()));
            stmt.execute();

            System.out.println("Cliente cadastrado com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());

            return false;
        }
    }

    public Cliente getCliente(String cpf) {
        String query = "SELECT * FROM cliente WHERE cpf = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            Cliente cliente = new Cliente();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return null;
            }

            cliente.setCpf(cpf);
            cliente.setSenha(rs.getString("senha"));
            cliente.setNome(rs.getString("nome"));
            cliente.setDataNasc(rs.getDate("dataNasc").toLocalDate());
            cliente.setSaldoDevedor(rs.getDouble("saldoDevedor"));

            return cliente;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public boolean update(String cpf, String senha, String nome, LocalDate dataNasc, double saldoDevedor) {
        String query = "UPDATE cliente SET senha=?, nome=?, dataNasc=?, saldoDevedor=? WHERE cpf=?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, senha);
            stmt.setString(2, nome);
            stmt.setDate(3, Date.valueOf(dataNasc));
            stmt.setDouble(4, saldoDevedor);
            stmt.setString(5, cpf);
            stmt.execute();

            System.out.println("Dados atualizados com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao autalizar: " + e.getMessage());

            return false;
        }
    }
}
