package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
    public void insert(Cliente cliente) throws Exception {
        String sql = "INSERT INTO cliente (cpf, senha, nome, dataNasc) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getSenha());
            stmt.setString(3, cliente.getNome());
            stmt.setDate(4, Date.valueOf(cliente.getDataNasc()));
            stmt.execute();

            System.out.println("Cliente cadastrado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar cliente: " + e);
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

    public void update(Cliente cliente)
            throws Exception {
        String query = "UPDATE cliente SET senha=?, nome=?, dataNasc=?, saldoDevedor=? WHERE cpf=?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cliente.getSenha());
            stmt.setString(2, cliente.getNome());
            stmt.setDate(3, Date.valueOf(cliente.getDataNasc()));
            stmt.setDouble(4, cliente.getSaldoDevedor());
            stmt.setString(5, cliente.getCpf());
            stmt.execute();

            System.out.println("Dados atualizados com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void delete(Cliente cliente) throws Exception {
        String query = "DELETE FROM cliente WHERE cpf=?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cliente.getCpf());
            stmt.execute();

            System.out.println("Cliente excluído com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao excluir: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> getListaClientes(int limit, int offset) throws Exception {
        String query = "SELECT * FROM cliente LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Cliente> clientes = new ArrayList<Cliente>();

            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setCpf(rs.getString("cpf"));
                cliente.setSenha(rs.getString("senha"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                cliente.setSaldoDevedor(rs.getDouble("saldoDevedor"));

                clientes.add(cliente);
            }

            return clientes;

        } catch (Exception e) {
            throw new Exception("Erro ao pesquisar: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> getClientesByCpfLike(String cpf, int limit, int offset) throws Exception {
        String query = "SELECT * FROM cliente WHERE cpf LIKE ? LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, "%" + cpf + "%");
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Cliente> clientes = new ArrayList<Cliente>();

            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setCpf(rs.getString("cpf"));
                cliente.setSenha(rs.getString("senha"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                cliente.setSaldoDevedor(rs.getDouble("saldoDevedor"));

                clientes.add(cliente);
            }

            return clientes;

        } catch (Exception e) {
            throw new Exception("Erro ao pesquisar: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> getClientesByNomeLike(String nome, int limit, int offset) throws Exception {
        String query = "SELECT * FROM cliente WHERE LOWER(nome) LIKE ? LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, "%" + nome.toLowerCase() + "%");
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Cliente> clientes = new ArrayList<Cliente>();

            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setCpf(rs.getString("cpf"));
                cliente.setSenha(rs.getString("senha"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDataNasc(rs.getDate("dataNasc").toLocalDate());
                cliente.setSaldoDevedor(rs.getDouble("saldoDevedor"));

                clientes.add(cliente);
            }

            return clientes;

        } catch (Exception e) {
            throw new Exception("Erro ao pesquisar: " + e.getMessage());
        }
    }
}
