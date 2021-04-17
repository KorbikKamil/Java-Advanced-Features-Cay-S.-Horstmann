package view;

import javax.swing.*;
import java.awt.*;

/**
 * Program wykorzystujący metadane do prezentacji dowolnych tabel bazy danych.
 * @author Cay Horstmann
 * @version 1.34 2018-05-01
 */
public class ViewDB {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new ViewDBFrame();
            frame.setTitle("ViewDB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
