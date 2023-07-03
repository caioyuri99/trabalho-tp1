package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Gibi;
import biblioteca.Item;
import biblioteca.Obra;
import connection.ConnectionDB;

public class GibiDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public GibiDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public void insert(Gibi gibi) throws Exception {
        String query = "INSERT INTO gibi (editora, edicao, condicao, obra, tipo) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, gibi.getEditora());
            stmt.setInt(2, gibi.getEdicao());
            stmt.setString(3, gibi.getCondicao());
            stmt.setInt(4, gibi.getObra().getId());
            stmt.setString(5, gibi.getTipo());
            stmt.execute();

            System.out.println("Gibi cadastrado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar: " + e.getMessage());

        }
    }

    public void update(Gibi gibi) throws Exception {
        String query = "UPDATE gibi SET editora = ?, edicao = ?, condicao = ?, obra = ?, tipo = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, gibi.getEditora());
            stmt.setInt(2, gibi.getEdicao());
            stmt.setString(3, gibi.getCondicao());
            stmt.setInt(4, gibi.getObra().getId());
            stmt.setString(5, gibi.getTipo());
            stmt.setInt(7, gibi.getId());
            stmt.execute();

            System.out.println("Gibi atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());

        }
    }

    public Gibi getGibi(int id) {
        String query = "SELECT * FROM gibi WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Gibi gibi = new Gibi();

            boolean res = rs.first();
            if (!res) {
                System.out.println("Gibi não encontrado!");

                return null;
            }

            gibi.setId(rs.getInt("id"));
            gibi.setEditora(rs.getString("editora"));
            gibi.setEdicao(rs.getInt("edicao"));
            gibi.setCondicao(rs.getString("condicao"));
            gibi.setDisponivel(rs.getBoolean("disponivel"));
            gibi.setObra(new ObraDAO().getObra(rs.getInt("obra")));
            gibi.setTipo(rs.getString("tipo"));

            return gibi;

        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());

            return null;

        }
    }

    public ArrayList<Item> getItemsOfObra(Obra obra) {
        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM gibi WHERE obra = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obra.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Gibi gibi = new Gibi();
                gibi.setId(rs.getInt("id"));
                gibi.setEditora(rs.getString("editora"));
                gibi.setEdicao(rs.getInt("edicao"));
                gibi.setCondicao(rs.getString("condicao"));
                gibi.setDisponivel(rs.getBoolean("disponivel"));
                gibi.setTipo(rs.getString("tipo"));
                gibi.setObra(obra);

                items.add(gibi);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());

            return null;

        }
    }

    public void delete(int id) throws Exception {
        String query = "DELETE FROM gibi WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();

            System.out.println("Gibi removido com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao remover: " + e.getMessage());

        }
    }

    public void updateLeitor(Gibi gibi, boolean disponivel) throws Exception {
        String query = "UPDATE gibi SET disponivel = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, gibi.getId());
            stmt.execute();

            System.out.println("Leitor atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());

        }
    }

    public ArrayList<Item> getEmprestados(Cliente leitor) {
        ArrayList<Item> items = new ArrayList<Item>();
        String query = "SELECT * FROM gibi WHERE leitor = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Gibi gibi = new Gibi();
                gibi.setId(rs.getInt("id"));
                gibi.setEditora(rs.getString("editora"));
                gibi.setEdicao(rs.getInt("edicao"));
                gibi.setCondicao(rs.getString("condicao"));
                gibi.setDisponivel(rs.getBoolean("disponivel"));
                gibi.setTipo(rs.getString("tipo"));
                gibi.setObra(new ObraDAO().getObra(rs.getInt("obra")));

                items.add(gibi);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());

            return null;

        }

    }

    public void closeConnection() {
        try {
            this.connection.close();

        } catch (Exception e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
