package view;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Ramka zawierająca panel danych i przyciski nawigacji.
 * @author Cay Horstmann
 */
public class ViewDBFrame extends JFrame {
    private JButton previousButton;
    private JButton nextButton;
    private JButton deleteButton;
    private JButton saveButton;
    private DataPanel dataPanel;
    private Component scrollPane;
    private JComboBox<String> tableNames;
    private Properties props;
    private CachedRowSet crs;
    private Connection conn;

    public ViewDBFrame() {
        tableNames = new JComboBox<String>();

        try {
            readDatabaseProperties();
            conn = getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet mrs = meta.getTables(null, null, null, new String[]{"TABLE"})) {
                while (mrs.next())
                    tableNames.addItem(mrs.getString(3));
            }
        } catch (SQLException ex) {
            for (Throwable t : ex)
                t.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        tableNames.addActionListener(event -> showTable((String) tableNames.getSelectedItem(), conn));
        add(tableNames, BorderLayout.NORTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    for (Throwable t : ex)
                        t.printStackTrace();
                }
            }
        });

        var buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        previousButton = new JButton("Poprzedni");
        previousButton.addActionListener(e -> showPreviousRow());
        buttonPanel.add(previousButton);

        nextButton = new JButton("Następny");
        nextButton.addActionListener(e -> showNextRow());
        buttonPanel.add(nextButton);

        deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(e -> deleteRow());
        buttonPanel.add(deleteButton);

        saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> saveChanges());
        buttonPanel.add(saveButton);
        if (tableNames.getItemCount() > 0)
            showTable(tableNames.getItemAt(0), conn);
    }

    /**
     * Przygotowuje pola tekstowe do prezetnacji nowej tabeli i wyświetla zawartość jej pierwszego rekordu.
     *
     * @param tableName nazwa prezentowanej tabeli.
     * @param conn      połączenie z bazą danych
     */
    public void showTable(String tableName, Connection conn) {
        try (Statement stat = conn.createStatement();
             ResultSet result = stat.executeQuery("SELECT * FROM " + tableName)) {
            //Pobranie zbioru wyników

            //Skopiowanie do zbioru przechowywanego w pamięci
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            crs.setTableName(tableName);
            crs.populate(result);

            if (scrollPane != null) remove(scrollPane);
            dataPanel = new DataPanel(crs);
            scrollPane = new JScrollPane(dataPanel);
            add(scrollPane, BorderLayout.CENTER);
            pack();
            showNextRow();

        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Pokazuje poprzedni rekord tabeli.
     */
    public void showPreviousRow() {
        try {
            if (crs == null || crs.isFirst()) return;
            crs.previous();
            dataPanel.setRow(crs);
        } catch (SQLException ex) {
            for (Throwable t : ex)
                t.printStackTrace();
        }
    }

    /**
     * Pokazuje następny rekord tabeli.
     */
    public void showNextRow() {
        try {
            if (crs == null || crs.isLast()) return;
            crs.next();
            dataPanel.setRow(crs);
        } catch (SQLException ex) {
            for (Throwable t : ex)
                t.printStackTrace();
        }
    }

    /**
     * Usuwa bieżący rekord tabeli.
     */
    public void deleteRow() {
        if (crs == null) return;
        new SwingWorker<Void, Void>() {
            public Void doInBackground() throws SQLException {
                crs.deleteRow();
                crs.acceptChanges(conn);
                if (crs.isAfterLast())
                    if (!crs.last()) crs = null;
                return null;
            }
        }.execute();
    }

    /**
     * Zapisuje wszystkie modyfikacje.
     */
    public void saveChanges() {
        if (crs == null) return;
        new SwingWorker<Void, Void>() {
            public Void doInBackground() throws SQLException {
                dataPanel.setRow(crs);
                crs.acceptChanges(conn);
                return null;
            }
        }.execute();
    }

    private void readDatabaseProperties() throws IOException {
        props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("5.Programowanie_baz_danych_JDBC/database.properties"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
    }

    /**
     * Nawiązuje połączenie, korzystając z właściwości zapisanych w pliku database.propertie
     *
     * @return połączenie z bazą danych
     */
    private Connection getConnection() throws SQLException {
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);

    }
}
