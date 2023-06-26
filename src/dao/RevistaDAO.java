package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Obra;
import biblioteca.Revista;
import connection.ConnectionDB;

public class RevistaDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public RevistaDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // METODOS
    public void insert(Revista revista) throws Exception {
        String query = "INSERT INTO revista (editora, edicao, condicao, obra, categoria) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, revista.getEditora());
            stmt.setInt(2, revista.getEdicao());
            stmt.setString(3, revista.getCondicao());
            stmt.setInt(4, revista.getObra().getId());
            stmt.setString(5, revista.getCategoria());
            stmt.execute();

            System.out.println("Revista cadastrada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public Revista getRevista(int id) {
        String query = "SELECT * FROM revista WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Revista revista = new Revista();

            boolean res = rs.first();
            if (!res) {
                System.out.println("Revista não encontrada!");

                return null;
            }

            revista.setId(rs.getInt("id"));
            revista.setEditora(rs.getString("editora"));
            revista.setEdicao(rs.getInt("edicao"));
            revista.setCondicao(rs.getString("condicao"));
            revista.setDisponivel(rs.getBoolean("disponivel"));
            revista.setObra(new ObraDAO().getObra(rs.getInt("obra")));
            revista.setCategoria(rs.getString("categoria"));

            return revista;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public ArrayList<Item> getItemsOfObra(Obra obra) {
        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM revista WHERE obra = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obra.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Revista revista = new Revista();
                revista.setId(rs.getInt("id"));
                revista.setEditora(rs.getString("editora"));
                revista.setEdicao(rs.getInt("edicao"));
                revista.setCondicao(rs.getString("condicao"));
                revista.setDisponivel(rs.getBoolean("disponivel"));
                revista.setObra(obra);
                revista.setCategoria(rs.getString("categoria"));

                items.add(revista);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM revista WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();

            System.out.println("Revista removida com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());

            return false;
        }
    }

    public void updateLeitor(Revista revista, boolean disponivel) throws Exception {
        String query = "UPDATE revista SET disponivel = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, revista.getId());
            stmt.execute();

            System.out.println("Leitor atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());
        }
    }

    public ArrayList<Item> getEmprestados(Cliente leitor) {
        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM revista WHERE leitor = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Revista revista = new Revista();
                revista.setId(rs.getInt("id"));
                revista.setEditora(rs.getString("editora"));
                revista.setEdicao(rs.getInt("edicao"));
                revista.setCondicao(rs.getString("condicao"));
                revista.setDisponivel(rs.getBoolean("disponivel"));
                revista.setObra(new ObraDAO().getObra(rs.getInt("obra")));
                revista.setCategoria(rs.getString("categoria"));

                items.add(revista);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }
}
