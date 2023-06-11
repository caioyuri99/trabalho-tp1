package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1/biblioteca",
                    "root",
                    "");

            return conn;
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e);

            return null;
        }
    }

}
