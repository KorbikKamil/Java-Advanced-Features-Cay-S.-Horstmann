package dateFormat;

import javax.swing.*;
import java.awt.*;

/**
 * Program demonstrujący formatowanie dat dla różnych lokalizatorów.
 * @version 1.01 2018-05-01
 * @author Cay Horstmann
 */
public class DateTimeFormaterTest {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            var frame = new DateTimeFormatterFrame();
            frame.setTitle("DateFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
