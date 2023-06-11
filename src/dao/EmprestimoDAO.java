package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import biblioteca.Cliente;
import biblioteca.Item;
import connection.ConnectionDB;

public class EmprestimoDAO {
    // ATRIBUTOS
    ConnectionDB connectionDB;
    Connection connection;

    // CONSTRUTORES
    public EmprestimoDAO() {
        this.connectionDB = new ConnectionDB();
        this.connection = this.connectionDB.getConnection();
    }

    // MÉTODOS
    public boolean insert(Cliente cliente, Item item) {
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

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao registrar: " + e.getMessage());

            return false;
        }
    }
}
