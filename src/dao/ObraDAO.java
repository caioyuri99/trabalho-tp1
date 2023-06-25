package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import biblioteca.Estante;
import biblioteca.Item;
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
            LocalDate toData, String genero, String disponibilidade, String condicao, String editora, int limit,
            int offset) {

        if (fromData == null) {
            fromData = LocalDate.of(1000, 1, 1);
        }

        if (toData == null) {
            toData = LocalDate.now();
        }

        String query = String.format(
                "SELECT * FROM obra WHERE (LOWER(nome) LIKE ? OR LOWER(autor) LIKE ?) AND tipo LIKE ? AND estante %s AND dataPublicacao BETWEEN ? AND ? AND genero LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?",
                (estante == null) ? "IS NOT NULL" : "= ?");

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            int c = 1;
            stmt.setString(c, '%' + search.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + search.toLowerCase() + '%');
            c++;
            stmt.setString(c, '%' + tipo + '%');
            c++;
            if (estante != null) {
                stmt.setInt(c, estante.getId());
                c++;
            }
            stmt.setDate(c, Date.valueOf(fromData));
            c++;
            stmt.setDate(c, Date.valueOf(toData));
            c++;
            stmt.setString(c, '%' + genero + '%');
            c++;
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

                if (!disponibilidade.equals("")) {
                    if (disponibilidade.equals("disponivel")) {
                        if (this.isDisponivel(obra)) {
                            continue;
                        }
                    } else if (disponibilidade.equals("indisponivel")) {
                        if (!this.isDisponivel(obra)) {
                            continue;
                        }
                    }
                }

                ArrayList<Item> itemsOfObra = switch (obra.getTipo()) {
                    case "livro" -> new LivroDAO().getItemsOfObra(obra);
                    case "revista" -> new RevistaDAO().getItemsOfObra(obra);
                    case "gibi" -> new GibiDAO().getItemsOfObra(obra);
                    default -> null;
                };

                if (!condicao.equals("")) {
                    boolean hasItemWithCondition = false;
                    for (Item item : itemsOfObra) {
                        if (item.getCondicao().equals(condicao)) {
                            hasItemWithCondition = true;
                            break;
                        }
                    }

                    if (!hasItemWithCondition) {
                        continue;
                    }
                }

                if (!editora.equals("")) {
                    boolean hasItemWithEditora = false;
                    for (Item item : itemsOfObra) {
                        if (item.getEditora().toLowerCase().contains(editora.toLowerCase())) {
                            hasItemWithEditora = true;
                            break;
                        }
                    }

                    if (!hasItemWithEditora) {
                        continue;
                    }
                }

                obras.add(obra);
            }

            return obras;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar: " + e.getMessage());

            return null;
        }
    }

}
