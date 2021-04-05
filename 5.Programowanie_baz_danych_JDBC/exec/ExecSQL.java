package exec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;
import java.util.Scanner;

/**
 * Program wykonuje polecenia języka SQL zapisane w pliku.
 * Uruchomienie programu:
 * java -classpath ścieżkaSterownika:. exec.ExecSQL plikPoleceń
 *
 * @author Cay Horstmann
 * @version 1.33 2018-05-01
 */
public class ExecSQL {

    public static void main(String[] args) throws IOException {
        try (Scanner in = args.length == 0 ? new Scanner(System.in) : new Scanner(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            try (Connection conn = getConnection(); Statement stat = conn.createStatement()) {
                while (true) {
                    if (args.length == 0) System.out.println("Wpisz polecenie lub EXIT, by zakończyć");

                    if (!in.hasNextLine()) return;

                    String line = in.nextLine().trim();
                    if (line.equalsIgnoreCase("EXIT")) return;
                    if (line.endsWith(";"))//Usuwamy średnik z końca łańcucha
                        line = line.substring(0, line.length() - 1);
                    try {
                        boolean isResult = stat.execute(line);
                        if(isResult){
                            try(ResultSet rs = stat.getResultSet()){
                                showResultSet(rs);
                            }
                        } else {
                            int updateCount = stat.getUpdateCount();
                            System.out.println("Zaktualizowano wierszy: " + updateCount);
                        }
                    } catch (SQLException e) {
                        for (Throwable t : e)
                            t.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            for(Throwable t : e)
                e.printStackTrace();
        }
    }

    /**
     * Nawiązuje połączenie, korzystając z właściwości zapisanych w pliku database.properties
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

    /**
     * Wyświetla wynik zapytania
     *
     * @param result zbiór wyników do wyświetlenia
     */
    public static void showResultSet(ResultSet result) throws SQLException {
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) System.out.println(", ");
            System.out.println(metaData.getColumnLabel(i));
        }
        System.out.println();

        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) System.out.println(", ");
                System.out.println(metaData.getColumnLabel(i));
            }
            System.out.println();
        }
    }
}
