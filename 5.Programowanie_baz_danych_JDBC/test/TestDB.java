package test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Program sprawdzający poprawność konfiguracji baz danych i sterownika JDBC.
 *
 * @author Cay Hortsmann
 * @version 1.03 2018-05-01
 */
public class TestDB {
    public static void main(String[] args) throws IOException {
        try {
            runTest();
        } catch (SQLException ex){
            for(Throwable t : ex)
                t.printStackTrace();
        }
    }

    /**
     * Wykonuje test polegający na utworzeniu tabeli, wstawieniu do niej wartości,
     * prezentacji zawartości i usunięciu tabeli.
     */
    public static void runTest() throws IOException, SQLException {
        try (Connection conn = getConnection(); Statement stat = conn.createStatement()) {
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");

            try (ResultSet result = stat.executeQuery("SELECT * FROM Greetings")) {
                if (result.next())
                    System.out.println(result.getString(1));
            }
            stat.executeUpdate("DROP TABLE Greetings");
        }
    }

    /**
     * Nawiązuje połączenie, korzystając z właściwości w pliku database.properties
     *
     * @return połączenie z bazą danych
     */
    public static Connection getConnection() throws IOException, SQLException {
        var props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("5.Programowanie_baz_danych_JDBC/database.properties"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);

    }
}
