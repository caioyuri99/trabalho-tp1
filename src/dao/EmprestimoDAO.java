package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Emprestimo;
import biblioteca.Gibi;
import biblioteca.Item;
import biblioteca.Livro;
import biblioteca.Revista;
import connection.ConnectionDB;

public class EmprestimoDAO {
    // TODO: ver a possibilidade de usar roolback para dar segurança ao banco de dados

    // ATRIBUTOS
    ConnectionDB connectionDB;
    Connection connection;

    // CONSTRUTORES
    public EmprestimoDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public void insert(Cliente cliente, Item item) throws Exception {
        String tipoItem = item.getClass().getSimpleName().toLowerCase();

        String query = String.format(
                "INSERT INTO emprestimo (tipoItem, %s, leitor, dataEmprestimo, dataDevolucao) VALUES (?, ?, ?, ?, ?)",
                tipoItem);

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, tipoItem);
            stmt.setInt(2, item.getId());
            stmt.setString(3, cliente.getCpf());
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            stmt.setDate(5, Date.valueOf(LocalDate.now().plusDays(14)));
            stmt.execute();

            System.out.println("Emprestimo registrado com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao registrar empréstimo: " + e.getMessage());
        }
    }

    public Item getItemFromEmprestimo(Emprestimo emprestimo) {
        String tipoItem = emprestimo.getTipoItem();

        Item item = null;
        switch (tipoItem) {
            case "livro":
                item = new Livro();
                break;

            case "revista":
                item = new Revista();
                break;

            case "gibi":
                item = new Gibi();
        }

        String query = String.format(
                "SELECT * FROM emprestimo INNER JOIN %s ON emprestimo.%s = %s.id WHERE emprestimo.id = ?",
                tipoItem, tipoItem, tipoItem);

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, emprestimo.getId());
            ResultSet rs = stmt.executeQuery();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return null;
            }

            item.setId(rs.getInt(tipoItem + ".id"));
            item.setEditora(rs.getString(tipoItem + ".editora"));
            item.setEdicao(rs.getInt(tipoItem + ".edicao"));
            item.setCondicao(rs.getString(tipoItem + ".condicao"));
            item.setDisponivel(rs.getBoolean(tipoItem + ".disponivel"));
            item.setObra(new ObraDAO().getObra(rs.getInt(tipoItem + ".obra")));
            item.setLeitor(new ClienteDAO().getCliente(rs.getString(tipoItem + ".leitor")));

            switch (tipoItem) {
                case "livro":
                    ((Livro) item).setTipoCapa(rs.getString(tipoItem + ".tipoCapa"));
                    break;

                case "revista":
                    ((Revista) item).setCategoria(rs.getString(tipoItem + ".categoria"));
                    break;

                case "gibi":
                    ((Gibi) item).setTipo(rs.getString(tipoItem + ".tipo"));
                    ((Gibi) item).setCategoria(rs.getString(tipoItem + ".categoria"));
            }

            return item;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public Emprestimo getEmprestimo(int id) {
        String query = "SELECT * FROM emprestimo WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Emprestimo emprestimo = new Emprestimo();

            boolean res = rs.first();
            if (!res) {
                System.out.println("A pesquisa não gerou resultados.");

                return null;
            }

            emprestimo.setId(rs.getInt("id"));
            emprestimo.setTipoItem(rs.getString("tipoItem"));
            emprestimo.setLeitor(new ClienteDAO().getCliente(rs.getString("leitor")));
            emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
            emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
            emprestimo.setQtdRenovacoes(rs.getInt("qtdRenovacoes"));
            emprestimo.setItem(this.getItemFromEmprestimo(emprestimo));
            emprestimo.setDevolvido(rs.getBoolean("devolvido"));
            emprestimo.setAtrasado(rs.getBoolean("atrasado"));
            emprestimo.setMultado(rs.getBoolean("multado"));
            emprestimo.setValorMulta(rs.getDouble("valorMulta"));

            return emprestimo;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return null;
        }
    }

    public int getTotalEmprestimos(Cliente leitor) {
        String query = "SELECT COUNT(*) AS total FROM emprestimo WHERE leitor = ? AND NOT devolvido";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            ResultSet rs = stmt.executeQuery();

            rs.first();

            return rs.getInt("total");

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());

            return 0;
        }
    }

    public boolean renovacao(int id) {
        String query = "UPDATE emprestimo SET dataDevolucao = ?, qtdRenovacoes = ? WHERE id = ?";

        try {
            Emprestimo emprestimo = this.getEmprestimo(id);

            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(emprestimo.getDataDevolucao().plusDays(7)));
            stmt.setInt(2, emprestimo.getQtdRenovacoes() + 1);
            stmt.setInt(3, id);
            stmt.execute();

            System.out.println("Renovacao registrada com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao registrar: " + e.getMessage());

            return false;
        }
    }

    public boolean devolucao(int id) {
        String query = "UPDATE emprestimo SET dataDevolucao = ?, devolvido = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setBoolean(2, true);
            stmt.setInt(3, id);
            stmt.execute();

            System.out.println("Devolucao registrada com sucesso!");

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao registrar: " + e.getMessage());

            return false;
        }
    }

    public ArrayList<Emprestimo> getEmprestimosAtivos(Cliente leitor) throws Exception {
        String query = "SELECT * FROM emprestimo WHERE leitor = ? AND NOT devolvido";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, leitor.getCpf());
            ResultSet rs = stmt.executeQuery();

            ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setId(rs.getInt("id"));
                emprestimo.setTipoItem(rs.getString("tipoItem"));
                emprestimo.setLeitor(leitor);
                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
                emprestimo.setQtdRenovacoes(rs.getInt("qtdRenovacoes"));
                emprestimo.setItem(this.getItemFromEmprestimo(emprestimo));
                emprestimo.setDevolvido(rs.getBoolean("devolvido"));
                emprestimo.setAtrasado(rs.getBoolean("atrasado"));
                emprestimo.setMultado(rs.getBoolean("multado"));
                emprestimo.setValorMulta(rs.getDouble("valorMulta"));

                emprestimos.add(emprestimo);
            }

            return emprestimos;

        } catch (Exception e) {
            throw new Exception("Erro ao obter: " + e.getMessage());
        }
    }
}
