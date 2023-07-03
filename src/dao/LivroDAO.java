package dao;

import java.sql.Connection;
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
    public void insert(Livro livro) throws Exception {
        String query = "INSERT INTO livro (editora, edicao, condicao, obra, tipoCapa) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, livro.getEditora());
            stmt.setInt(2, livro.getEdicao());
            stmt.setString(3, livro.getCondicao());
            stmt.setInt(4, livro.getObra().getId());
            stmt.setString(5, livro.getTipoCapa().toLowerCase());
            stmt.execute();

            System.out.println("Livro cadastrado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar: " + e.getMessage());

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public void update(Livro livro) throws Exception {
        String query = "UPDATE livro SET editora = ?, edicao = ?, condicao = ?, obra = ?, tipoCapa = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, livro.getEditora());
            stmt.setInt(2, livro.getEdicao());
            stmt.setString(3, livro.getCondicao());
            stmt.setInt(4, livro.getObra().getId());
            stmt.setString(5, livro.getTipoCapa().toLowerCase());
            stmt.setInt(6, livro.getId());
            stmt.execute();

            System.out.println("Livro atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
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
            String tipoCapa = rs.getString("tipoCapa");
            livro.setTipoCapa(tipoCapa.substring(0, 1).toUpperCase() + tipoCapa.substring(1).toLowerCase());

            return livro;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
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
                String tipoCapa = rs.getString("tipoCapa");
                livro.setTipoCapa(tipoCapa.substring(0, 1).toUpperCase() + tipoCapa.substring(1).toLowerCase());

                items.add(livro);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public void delete(int id) throws Exception {
        String query = "DELETE FROM livro WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();

            System.out.println("Livro removido com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao remover: " + e.getMessage());

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public void updateLeitor(Livro livro, boolean disponivel) throws Exception {
        String query = "UPDATE livro SET disponivel = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, livro.getId());
            stmt.execute();

            System.out.println("Leitor atualizado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
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

                items.add(livro);
            }

            return items;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;

        } finally {
            try {
                this.connection.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
