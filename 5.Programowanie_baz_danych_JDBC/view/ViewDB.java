package view;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ViewDB {


}

class ViewDBFrame extends JFrame {
    private JButton previouseButton;
    private JButton nextButton;
    private JButton deleteButton;
    private JButton saveButton;
    private DataPanel dataPanel;
    private Component scrollPane;
    private JComboBox<String> tableNames;
    private Properties props;
    private CachedRowSet crs;
    private Connection conn;
}

class DataPanel extends JPanel {
    private java.util.List<JTextField> fields;

    /**
     * Pokazuje rekord bazy danych, wypełniając pola tekstowe danymi z kolejnych kolumn.
     */
    public void showRow(ResultSet rs) {
        try {
            if (rs == null) return;
            for (int i = 1; i <= fields.size(); i++) {
                String field = rs == null ? "" : rs.getString(i);
                JTextField tb = fields.get(i - 1);
                tb.setText(field);
            }
        } catch (SQLException ex) {
            for (Throwable t : ex)
                t.printStackTrace();
        }
    }

    /**
     * Aktualizuje dane zmodyfikowane w bieżącym rekordzie
     */
    public void setRow(RowSet rs) throws SQLException {
        for (int i = 1; i <= fields.size(); i++) {
            String field = rs.getString(i);
            JTextField tb = fields.get(i - 1);
            if (!field.equals(tb.getText()))
                rs.updateString(i, tb.getText());
        }
        rs.updateRow();
    }
}