package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import biblioteca.Estante;
import connection.ConnectionDB;

public class EstanteDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public EstanteDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public void insert(Estante estante) throws Exception {
        String sql = "INSERT INTO estante (categoria) VALUES (?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, estante.getCategoria());
            stmt.execute();

            System.out.println("Estante cadastrada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar estante: " + e.getMessage());
        }
    }

    public void update(Estante estante) throws Exception {
        String sql = "UPDATE estante SET categoria = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, estante.getCategoria());
            stmt.setInt(2, estante.getId());
            stmt.execute();

            System.out.println("Estante atualizada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar estante: " + e.getMessage());
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM estante WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();

            System.out.println("Estante deletada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao deletar estante: " + e.getMessage());
        }
    }

    public Estante getEstante(int id) {
        String query = "SELECT * FROM estante WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Estante estante = new Estante();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return new Estante("Sem Estante");
            }

            estante.setId(id);
            estante.setCategoria(rs.getString("categoria"));

            return estante;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public ArrayList<Estante> getAll() {
        String query = "SELECT * FROM estante";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Estante> estantes = new ArrayList<Estante>();

            while (rs.next()) {
                Estante estante = new Estante();
                estante.setId(rs.getInt("id"));
                estante.setCategoria(rs.getString("categoria"));
                estantes.add(estante);
            }

            return estantes;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public boolean estanteExists(String categoria) {
        String query = "SELECT * FROM estante WHERE categoria = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return false;
            }

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return false;
        }
    }
}
