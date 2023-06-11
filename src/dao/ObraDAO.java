package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import biblioteca.Estante;
import biblioteca.Obra;
import connection.ConnectionDB;

public class ObraDAO {
    // ATRIBUTOS
    private ConnectionDB connectionDB;
    private Connection connection;

    // CONSTRUTORES
    public ObraDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public boolean insert(Obra obra, Estante estante) {
        String query = "INSERT INTO obra (nome, tipo, dataPublicacao, autor, volume, genero, classIndicativa, estante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, obra.getNome());
            stmt.setString(2, obra.getTipo());
            stmt.setDate(3, Date.valueOf(obra.getDataPublicacao()));
            stmt.setString(4, obra.getAutor());
            stmt.setInt(5, obra.getVolume());
            stmt.setString(6, obra.getGenero());
            stmt.setInt(7, obra.getClassificaoIndicativa());
            stmt.setInt(8, estante.getId());
            stmt.execute();

            System.out.println("Obra cadastrada com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());

            return false;
        }
    }

    public Obra getObra(int id) {
        String query = "SELECT * FROM obra WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Obra obra = new Obra();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return null;
            }

            obra.setId(id);
            obra.setNome(rs.getString("nome"));
            obra.setTipo(rs.getString("tipo"));
            obra.setDataPublicacao(rs.getDate("dataPublicacao").toLocalDate());
            obra.setAutor(rs.getString("autor"));
            obra.setVolume(rs.getInt("volume"));
            obra.setGenero(rs.getString("genero"));
            obra.setClassificaoIndicativa(rs.getInt("classIndicativa"));

            switch (obra.getTipo()) {
                case "livro":
                    LivroDAO livroDAO = new LivroDAO();
                    obra.setItens(livroDAO.getItemsOfObra(obra));

                    break;

                case "revista":
                    RevistaDAO revistaDAO = new RevistaDAO();
                    obra.setItens(revistaDAO.getItemsOfObra(obra));

                    break;

                case "gibi":
                    GibiDAO gibiDAO = new GibiDAO();
                    obra.setItens(gibiDAO.getItemsOfObra(obra));
            }

            obra.setEstante(new EstanteDAO().getEstante(rs.getInt("estante")));

            return obra;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM obra WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            boolean res = stmt.execute();

            if (!res) {
                System.out.println("Erro ao deletar.");

                return false;
            }

            System.out.println("Obra deletada com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao deletar: " + e.getMessage());

            return false;
        }
    }

    public boolean isDisponivel(Obra obra) {
        String query = String.format("SELECT * FROM %s WHERE obra = ? AND disponivel = true",
                obra.getTipo().toLowerCase());

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obra.getId());
            ResultSet rs = stmt.executeQuery();

            return rs.first();

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return false;
        }
    }
}
