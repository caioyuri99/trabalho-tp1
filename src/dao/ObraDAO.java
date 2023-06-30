package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public void insert(Obra obra, Estante estante) throws Exception {
        String query = "INSERT INTO obra (nome, tipo, dataPublicacao, autor, genero, sinopse, capaUrl, estante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, obra.getNome());
            stmt.setString(2, obra.getTipo());
            stmt.setDate(3, Date.valueOf(obra.getDataPublicacao()));
            stmt.setString(4, obra.getAutor());
            stmt.setString(5, obra.getGenero());
            stmt.setString(6, obra.getSinopse());
            stmt.setString(7, obra.getCapaUrl());
            stmt.setInt(8, estante.getId());
            stmt.execute();

            System.out.println("Obra cadastrada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public void updateObra(Estante estante, Obra obra) throws Exception {
        String query = "UPDATE obra SET nome = ?, tipo = ?, dataPublicacao = ?, autor = ?, genero = ?, sinopse = ?, capaUrl = ?, estante = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, obra.getNome());
            stmt.setString(2, obra.getTipo());
            stmt.setDate(3, Date.valueOf(obra.getDataPublicacao()));
            stmt.setString(4, obra.getAutor());
            stmt.setString(5, obra.getGenero());
            stmt.setString(6, obra.getSinopse());
            stmt.setString(7, obra.getCapaUrl());
            stmt.setInt(8, estante.getId());
            stmt.setInt(8, obra.getId());
            stmt.execute();

            System.out.println("Obra atualizada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());
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
            obra.setGenero(rs.getString("genero"));
            obra.setSinopse(rs.getString("sinopse"));
            obra.setCapaUrl(rs.getString("capaUrl"));
            obra.setEstante(new EstanteDAO().getEstante(rs.getInt("estante")));

            return obra;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public void delete(int id) throws Exception {
        String query = "DELETE FROM obra WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();

        } catch (Exception e) {
            throw new Exception("Erro ao deletar: " + e.getMessage());
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

    public ArrayList<Obra> getAll(int limit, int offset) {
        String query = "SELECT * FROM `obra` ORDER BY id ASC LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Obra> obras = new ArrayList<Obra>();

            while (rs.next()) {
                Obra obra = new Obra();
                obra.setId(rs.getInt("id"));
                obra.setNome(rs.getString("nome"));
                obra.setTipo(rs.getString("tipo"));
                obra.setDataPublicacao(rs.getDate("dataPublicacao").toLocalDate());
                obra.setAutor(rs.getString("autor"));
                obra.setGenero(rs.getString("genero"));
                obra.setSinopse(rs.getString("sinopse"));
                obra.setCapaUrl(rs.getString("capaUrl"));
                obra.setEstante(new EstanteDAO().getEstante(rs.getInt("estante")));
                obras.add(obra);
            }

            return obras;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public ArrayList<Obra> getObrasByNameOrAutor(String search, int limit, int offset) {
        String query = "SELECT * FROM obra WHERE LOWER(nome) LIKE ? OR LOWER(autor) LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, '%' + search.toLowerCase() + '%');
            stmt.setString(2, '%' + search.toLowerCase() + '%');
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Obra> obras = new ArrayList<Obra>();

            while (rs.next()) {
                Obra obra = new Obra();
                obra.setId(rs.getInt("id"));
                obra.setNome(rs.getString("nome"));
                obra.setTipo(rs.getString("tipo"));
                obra.setDataPublicacao(rs.getDate("dataPublicacao").toLocalDate());
                obra.setAutor(rs.getString("autor"));
                obra.setGenero(rs.getString("genero"));
                obra.setSinopse(rs.getString("sinopse"));
                obra.setCapaUrl(rs.getString("capaUrl"));
                obra.setEstante(new EstanteDAO().getEstante(rs.getInt("estante")));
                obras.add(obra);
            }

            return obras;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    public ArrayList<Obra> searchCustomQuery(String search, String tipo, Estante estante, LocalDate fromData,
            LocalDate toData, String genero, Boolean disponibilidade, String condicao, String editora, int limit,
            int offset) {

        String query = queryBuilder(estante, fromData, toData, disponibilidade);

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            int c = 1;
            stmt.setString(c, '%' + search.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + search.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + tipo.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + genero.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + editora.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + editora.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + editora.toLowerCase() + '%');
            c++;

            if (estante != null) {
                stmt.setInt(c, estante.getId());
                c++;
            }

            if (fromData != null && toData != null) {
                stmt.setDate(c, Date.valueOf(fromData));
                c++;
                stmt.setDate(c, Date.valueOf(toData));
                c++;
            } else if (fromData != null) {
                stmt.setDate(c, Date.valueOf(fromData));
                c++;
            } else if (toData != null) {
                stmt.setDate(c, Date.valueOf(toData));
                c++;
            }

            stmt.setInt(c, limit);
            c++;
            stmt.setInt(c, offset);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Obra> obras = new ArrayList<Obra>();

            while (rs.next()) {
                Obra obra = new Obra();
                obra.setId(rs.getInt("id"));
                obra.setNome(rs.getString("nome"));
                obra.setTipo(rs.getString("tipo"));
                obra.setDataPublicacao(rs.getDate("dataPublicacao").toLocalDate());
                obra.setAutor(rs.getString("autor"));
                obra.setGenero(rs.getString("genero"));
                obra.setSinopse(rs.getString("sinopse"));
                obra.setCapaUrl(rs.getString("capaUrl"));
                obra.setEstante(new EstanteDAO().getEstante(rs.getInt("estante")));

                obras.add(obra);
            }

            return obras;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

    private String queryBuilder(Estante estante, LocalDate dateFrom, LocalDate dateTo, Boolean disponibilidade) {
        String query = "SELECT DISTINCT obra.* FROM obra LEFT JOIN livro ON obra.tipo = 'livro' AND livro.obra = obra.id LEFT JOIN revista ON obra.tipo = 'revista' AND revista.obra = obra.id LEFT JOIN gibi ON obra.tipo = 'gibi' AND gibi.obra = obra.id WHERE (LOWER(obra.nome) LIKE ? OR LOWER(obra.autor) LIKE ?) AND LOWER(obra.tipo) LIKE ? AND LOWER(obra.genero) LIKE ? AND (LOWER(livro.editora) LIKE ? OR LOWER(revista.editora) LIKE ? OR LOWER(gibi.editora) LIKE ?) ";

        if (estante != null) {
            query += "AND obra.estante = ? ";
        }

        if (dateFrom != null && dateTo != null) {
            query += "AND obra.dataPublicacao BETWEEN ? AND ? ";
        } else if (dateFrom != null) {
            query += "AND obra.dataPublicacao >= ? ";
        } else if (dateTo != null) {
            query += "AND obra.dataPublicacao <= ? ";
        }

        if (disponibilidade != null) {
            if (disponibilidade) {
                query += "(livro.disponivel = true OR revista.disponivel = true OR gibi.disponivel = true)";
            } else {
                query += "AND ((obra.tipo = 'livro' AND NOT EXISTS (SELECT 1 FROM livro WHERE livro.obra = obra.id AND livro.disponivel = true)) OR (obra.tipo = 'revista' AND NOT EXISTS (SELECT 1 FROM revista WHERE revista.obra = obra.id AND revista.disponivel = true)) OR (obra.tipo = 'gibi' AND NOT EXISTS (SELECT 1 FROM gibi WHERE gibi.obra = obra.id AND gibi.disponivel = true))) ";
            }
        }

        query += "ORDER BY obra.id ASC LIMIT ? OFFSET ?";

        return query;
    }

}
