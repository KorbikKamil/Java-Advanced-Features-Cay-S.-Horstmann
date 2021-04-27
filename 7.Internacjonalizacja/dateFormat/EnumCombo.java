package dateFormat;

import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Lista rozwijana pozwalająca użytkownikowi wybrać jedną spośród warttości pól statycznych,
 * których nazwy zostały przekazane w wywołaniu konstruktora
 *
 * @author Cay Horstmann
 * @version 1.15 2016-05-06
 */
public class EnumCombo<T> extends JComboBox<String> {
    private Map<String, T> table = new TreeMap<>();

    /**
     * Konstruktor klasy EnumCombo zwracający wartość typu T.
     *
     * @param cl     klasa
     * @param labels tablica łańcuchów opisujących pola statyczne typu T
     */
    public EnumCombo(Class<T> cl, String... labels) {
        for (String label : labels) {
            String name = label.toUpperCase().replace(' ', '_');
            try {
                java.lang.reflect.Field f = cl.getField(name);
                @SuppressWarnings("unchecked") T value = (T) f.get(cl);
                table.put(label, value);
            } catch (Exception e) {
                label = "(" + label + ")";
                table.put(label, null);
            }
            addItem(label);
        }
        setSelectedItem(labels[0]);
    }

    /**
     * Zwraca wartość pola wybranego przez użytkownika
     *
     * @return wartość pola statycznego
     */
    public T getValue() {
        return table.get(getSelectedItem());
    }
}
