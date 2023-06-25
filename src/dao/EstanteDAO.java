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
    public boolean insert(Estante estante) {
        String sql = "INSERT INTO estante (categoria) VALUES (?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, estante.getCategoria());
            stmt.execute();

            System.out.println("Estante cadastrada com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());

            return false;
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

                return null;
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
}
