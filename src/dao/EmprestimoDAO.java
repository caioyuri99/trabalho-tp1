package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Emprestimo;
import biblioteca.Gibi;
import biblioteca.Item;
import biblioteca.Livro;
import biblioteca.Revista;
import connection.ConnectionDB;
import session.Session;

public class EmprestimoDAO {
    // TODO: ver a possibilidade de usar roolback para dar segurança ao banco de
    // dados

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

    public void pagarMulta(Emprestimo emprestimo) throws Exception {
        String query = "UPDATE emprestimo SET pago = true WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, emprestimo.getId());
            stmt.execute();

            System.out.println("Multa paga com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao pagar multa: " + e.getMessage());
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

            switch (tipoItem) {
                case "livro":
                    ((Livro) item).setTipoCapa(rs.getString(tipoItem + ".tipoCapa"));
                    break;

                case "revista":
                    ((Revista) item).setCategoria(rs.getString(tipoItem + ".categoria"));
                    ((Revista) item).setNumero(rs.getInt(tipoItem + ".numero"));
                    break;

                case "gibi":
                    ((Gibi) item).setTipo(rs.getString(tipoItem + ".tipo"));
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
            if (rs.wasNull()) {
                emprestimo.setValorMulta(null);
            }

            emprestimo.setPago(rs.getBoolean("pago"));
            if (rs.wasNull()) {
                emprestimo.setPago(null);
            }

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

    public void renovacao(int id) throws Exception {
        String query = "UPDATE emprestimo SET dataDevolucao = ?, qtdRenovacoes = ? WHERE id = ?";

        try {
            Emprestimo emprestimo = this.getEmprestimo(id);

            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(emprestimo.getDataDevolucao().plusDays(7)));
            stmt.setInt(2, emprestimo.getQtdRenovacoes() + 1);
            stmt.setInt(3, id);
            stmt.execute();

            System.out.println("Renovacao registrada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao registrar: " + e.getMessage());
        }
    }

    public void devolucao(int id) throws Exception {
        String query = "UPDATE emprestimo SET dataDevolucao = ?, devolvido = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setBoolean(2, true);
            stmt.setInt(3, id);
            stmt.execute();

            System.out.println("Devolucao registrada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao registrar: " + e.getMessage());
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
                if (rs.wasNull()) {
                    emprestimo.setValorMulta(null);
                }

                emprestimo.setPago(rs.getBoolean("pago"));
                if (rs.wasNull()) {
                    emprestimo.setPago(null);
                }

                emprestimos.add(emprestimo);
            }

            return emprestimos;

        } catch (Exception e) {
            throw new Exception("Erro ao obter: " + e.getMessage());
        }
    }

    public double getTotalMultas(Cliente cliente) throws Exception {
        String query = "SELECT SUM(valorMulta) AS total FROM emprestimo WHERE leitor = ? AND multado AND NOT pago";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cliente.getCpf());
            ResultSet rs = stmt.executeQuery();

            rs.first();

            return rs.getDouble("total");

        } catch (Exception e) {
            throw new Exception("Erro ao obter: " + e.getMessage());
        }
    }

    public void pagarMulta(Cliente cliente, Emprestimo emprestimo) throws Exception {
        String query = "UPDATE emprestimo SET pago = ?, devolvido = ? WHERE leitor = ? AND id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setBoolean(1, true);
            stmt.setBoolean(2, true);
            stmt.setString(3, cliente.getCpf());
            stmt.setInt(4, emprestimo.getId());
            stmt.execute();

            System.out.println("Multa paga com sucesso!");

        } catch (Exception e) {
            throw new Exception("Erro ao pagar: " + e.getMessage());
        }
    }

    public void atualizaAtrasosMultas(Cliente cliente) throws Exception {
        String query = "UPDATE emprestimo SET atrasado = ?, multado = ?, valorMulta = ?, pago = ? WHERE leitor = ? AND NOT devolvido";

        try {
            ArrayList<Emprestimo> emprestimos = this.getEmprestimosAtivos(cliente);

            for (Emprestimo emprestimo : emprestimos) {
                long atraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucao(), Session.getDataAtual());

                if (atraso <= 3 && atraso > 0) {
                    emprestimo.setAtrasado(true);
                    emprestimo.setMultado(false);
                    emprestimo.setValorMulta(null);
                    emprestimo.setPago(null);
                } else if (atraso > 3) {
                    emprestimo.setAtrasado(true);
                    emprestimo.setMultado(true);
                    emprestimo.setValorMulta(atraso * 0.8);
                    emprestimo.setPago(false);
                } else {
                    emprestimo.setAtrasado(false);
                    emprestimo.setMultado(false);
                    emprestimo.setValorMulta(null);
                    emprestimo.setPago(null);
                }

                PreparedStatement stmt = this.connection.prepareStatement(query);
                stmt.setBoolean(1, emprestimo.isAtrasado());
                stmt.setBoolean(2, emprestimo.isMultado());

                if (emprestimo.getValorMulta() == null) {
                    stmt.setNull(3, Types.DECIMAL);
                } else {
                    stmt.setDouble(3, emprestimo.getValorMulta());
                }

                if (emprestimo.isPago() == null) {
                    stmt.setNull(4, Types.TINYINT);
                } else {
                    stmt.setBoolean(4, emprestimo.isPago());
                }

                stmt.setString(5, cliente.getCpf());
                stmt.execute();
            }

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());
        }
    }

    public ArrayList<Emprestimo> getHistoricoEmprestimos(Cliente cliente, int limit, int offset) {
        String query = "SELECT * FROM emprestimo WHERE leitor = ? ORDER BY dataEmprestimo DESC LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cliente.getCpf());
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setId(rs.getInt("id"));
                emprestimo.setTipoItem(rs.getString("tipoItem"));
                emprestimo.setItem(this.getItemFromEmprestimo(emprestimo));
                emprestimo.setLeitor(cliente);
                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
                emprestimo.setQtdRenovacoes(rs.getInt("qtdRenovacoes"));
                emprestimo.setDevolvido(rs.getBoolean("devolvido"));
                emprestimo.setAtrasado(rs.getBoolean("atrasado"));
                emprestimo.setMultado(rs.getBoolean("multado"));

                emprestimo.setValorMulta(rs.getDouble("valorMulta"));
                if (rs.wasNull()) {
                    emprestimo.setValorMulta(null);
                }
                emprestimo.setPago(rs.getBoolean("pago"));
                if (rs.wasNull()) {
                    emprestimo.setPago(null);
                }

                emprestimos.add(emprestimo);
            }

            return emprestimos;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());
            return new ArrayList<Emprestimo>();
        }
    }

    public ArrayList<Emprestimo> filtraHistoricoEmprestimos(Cliente cliente, String search, LocalDate fromDate,
            LocalDate toDate, int limit, int offset) {

        String query = "SELECT emprestimo.* FROM emprestimo LEFT JOIN livro ON emprestimo.tipoItem = 'livro' AND emprestimo.livro = livro.id LEFT JOIN revista ON emprestimo.tipoItem = 'revista' AND emprestimo.revista = revista.id LEFT JOIN gibi ON emprestimo.tipoItem = 'gibi' AND emprestimo.gibi = gibi.id LEFT JOIN obra ON (emprestimo.tipoItem = 'livro' AND livro.obra = obra.id) OR (emprestimo.tipoItem = 'revista' AND revista.obra = obra.id) OR (emprestimo.tipoItem = 'gibi' AND gibi.obra = obra.id) WHERE emprestimo.leitor = ? AND ((LOWER(obra.nome) LIKE ? OR LOWER(obra.autor) LIKE ?)) AND emprestimo.dataEmprestimo BETWEEN ? AND ? ORDER BY emprestimo.dataEmprestimo DESC LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, "%" + search.toLowerCase() + "%");
            stmt.setString(3, "%" + search.toLowerCase() + "%");
            stmt.setDate(4, Date.valueOf(fromDate));
            stmt.setDate(5, Date.valueOf(toDate));
            stmt.setInt(6, limit);
            stmt.setInt(7, offset);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();

                emprestimo.setId(rs.getInt("id"));
                emprestimo.setTipoItem(rs.getString("tipoItem"));
                emprestimo.setItem(this.getItemFromEmprestimo(emprestimo));
                emprestimo.setLeitor(cliente);
                emprestimo.setDataEmprestimo(rs.getDate("dataEmprestimo").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
                emprestimo.setQtdRenovacoes(rs.getInt("qtdRenovacoes"));
                emprestimo.setDevolvido(rs.getBoolean("devolvido"));
                emprestimo.setAtrasado(rs.getBoolean("atrasado"));
                emprestimo.setMultado(rs.getBoolean("multado"));

                emprestimo.setValorMulta(rs.getDouble("valorMulta"));
                if (rs.wasNull()) {
                    emprestimo.setValorMulta(null);
                }

                emprestimo.setPago(rs.getBoolean("pago"));
                if (rs.wasNull()) {
                    emprestimo.setPago(null);
                }

                emprestimos.add(emprestimo);
            }

            return emprestimos;

        } catch (Exception e) {
            System.out.println("Erro ao obter: " + e.getMessage());
            return null;
        }
    }

}
