package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Livro;
import biblioteca.Obra;
import connection.ConnectionDB;

public class LivroDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTOR
    public LivroDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public boolean insert(Livro livro) {
        String query = "INSERT INTO livro (editora, edicao, condicao, obra, tipoCapa, dataLancamento) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, livro.getEditora());
            stmt.setInt(2, livro.getEdicao());
            stmt.setString(3, livro.getCondicao());
            stmt.setInt(4, livro.getObra().getId());
            stmt.setString(5, livro.getTipoCapa().toLowerCase());
            stmt.setDate(6, Date.valueOf(livro.getDataLancamento()));
            stmt.execute();

            System.out.println("Livro cadastrado com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());

            return false;
        }
    }

    public Livro getLivro(int id) {
        String query = "SELECT * FROM livro WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Livro livro = new Livro();

            boolean res = rs.first();
            if (!res) {
                System.out.println("Livro não encontrado!");

                return null;
            }

            livro.setId(rs.getInt("id"));
            livro.setEditora(rs.getString("editora"));
            livro.setEdicao(rs.getInt("edicao"));
            livro.setCondicao(rs.getString("condicao"));
            livro.setDisponivel(rs.getBoolean("disponivel"));
            livro.setObra(new ObraDAO().getObra(rs.getInt("obra")));
            livro.setLeitor(new ClienteDAO().getCliente(rs.getString("leitor")));
            livro.setTipoCapa(rs.getString("tipoCapa"));
            livro.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());

            return livro;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public ArrayList<Item> getItemsOfObra(Obra obra) {
        ArrayList<Item> items = new ArrayList<Item>();

        String query = "SELECT * FROM livro WHERE obra = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obra.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setEditora(rs.getString("editora"));
                livro.setEdicao(rs.getInt("edicao"));
                livro.setCondicao(rs.getString("condicao"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livro.setObra(obra);
                livro.setTipoCapa(rs.getString("tipoCapa"));
                livro.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());

                items.add(livro);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM livro WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();

            System.out.println("Livro removido com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());

            return false;
        }
    }

    public boolean updateLeitor(Livro livro, Cliente leitor, boolean disponivel) {
        String query = "UPDATE livro SET leitor = ?, disponivel = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            stmt.setBoolean(2, disponivel);
            stmt.setInt(3, livro.getId());
            stmt.execute();

            System.out.println("Leitor atualizado com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao atualizar leitor: " + e.getMessage());

            return false;
        }
    }

    public ArrayList<Item> getEmprestados(Cliente leitor) {
        ArrayList<Item> items = new ArrayList<Item>();

        String query = "SELECT * FROM livro WHERE leitor = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setEditora(rs.getString("editora"));
                livro.setEdicao(rs.getInt("edicao"));
                livro.setCondicao(rs.getString("condicao"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livro.setObra(new ObraDAO().getObra(rs.getInt("obra")));
                livro.setTipoCapa(rs.getString("tipoCapa"));
                livro.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
                livro.setLeitor(leitor);

                items.add(livro);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }
}
