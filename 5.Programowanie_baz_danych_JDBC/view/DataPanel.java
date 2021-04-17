package view;

import javax.sql.RowSet;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Panel wyświetlający zawartość zbioru rekordów.
 * @author Cay Horstmann
 */
public class DataPanel extends JPanel {
    private java.util.List<JTextField> fields;

    /**
     * Tworzy panel danych.
     * @param rs zbiór rekordów prezentowany przez panel.
     */
    public DataPanel(RowSet rs) throws SQLException {
        fields = new ArrayList<>();
        setLayout(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        ResultSetMetaData rsmd = rs.getMetaData();
        for(int i = 1; i<=rsmd.getColumnCount(); i++ ){
            gbc.gridy =  i - 1;

            String columnName = rsmd.getColumnLabel(i);
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel(columnName), gbc);

            int columnWidth = rsmd.getColumnDisplaySize(i);
            var tb = new JTextField(columnWidth);
            if(!rsmd.getColumnClassName(i).equals("java.lang.String"))
                tb.setEditable(false);

            fields.add(tb);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(tb, gbc);
        }
    }

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
     * Aktualizuje dane zmodyfikowane w bieżącym rekordzie.
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
