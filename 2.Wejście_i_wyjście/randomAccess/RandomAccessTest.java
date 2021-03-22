package randomAccess;

import java.io.*;
import java.time.*;

/**
 * @version 1.14 2018-05-01
 * @author Cay Horstmann
 */
public class RandomAccessTest {
    public static void main(String[] args) throws IOException {
        var staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        try (var out = new DataOutputStream(new FileOutputStream("2.Wejście_i_wyjście/employeeRandomAccess.dat"))) {
            //Zapisuje rekordy wszystkich pracowników w pliku employee.dat
            for (Employee e : staff)
                writeData(out, e);
        }

        try (var in = new RandomAccessFile("2.Wejście_i_wyjście/employeeRandomAccess.dat", "r")) {
            //Wczytuje wszystkie rekordy do nowej tablicy

            //Oblicza rozmiar tablicy
            int n = (int) (in.length() / Employee.RECORD_SIZE);
            var newStaff = new Employee[n];

            //Wczytuje rekordy pracowników w odwrotnej kolejności
            for (int i = n - 1; i >= 0; i--) {
                newStaff[i] = new Employee();
                in.seek(i * Employee.RECORD_SIZE);
                newStaff[i] = readData(in);
            }

            //Wyświetla wczytane rekordy
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }

    /**
     * Zapisuje dane pracownika
     *
     * @param out obiekt typu DataOutput
     * @param e   pracownik
     */
    public static void writeData(DataOutput out, Employee e) throws IOException {
        DataIO.writeFixedString(e.getName(), Employee.NAME_SIZE, out);
        out.writeDouble(e.getSalary());

        LocalDate hireDay = e.getHireDay();
        out.writeInt(hireDay.getYear());
        out.writeInt(hireDay.getMonthValue());
        out.writeInt(hireDay.getDayOfMonth());
    }

    /**
     * Wczytuje dane pracownika
     *
     * @param in obiekt typu DataInput
     * @return pracownik
     */
    public static Employee readData(DataInput in) throws IOException {
        String name = DataIO.readFixedString(Employee.NAME_SIZE, in);
        double salary = in.readDouble();
        int y = in.readInt();
        int m = in.readInt();
        int d = in.readInt();
        return new Employee(name, salary, y, m - 1, d);
    }
}
