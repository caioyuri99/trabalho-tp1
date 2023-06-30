package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/biblioteca?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC",
                    "root",
                    "");

            return conn;
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e);

            return null;
        }
    }
}
