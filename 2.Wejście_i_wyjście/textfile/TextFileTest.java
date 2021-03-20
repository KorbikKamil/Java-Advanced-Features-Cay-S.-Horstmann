package textfile;

/**
 * @version 1.15 2018-03-17
 * @author Cay Horstmann
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

public class TextFileTest {
    public static void main(String[] args) throws IOException {
        var staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        //zapisuje wszystkie rekordy pracowników w pliku employee.dat
        try (var out = new PrintWriter("employee.dat", StandardCharsets.UTF_8)) {
            writeData(staff, out);
        }

        //wczytuje wszystkie rekordy do nowej tablicy
        try (var in = new Scanner(
                new FileInputStream("employee.dat"), "UTF-8")) {
            Employee[] newStaff = readData(in);

            //Wyświetla wszystkie wczytane rekordy
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }

    /**
     * Zapisuje dane wszystkich obiektów klasy Employee umieszczonych
     *
     * @param employees tablica wszystkich obiektów klasy Employee
     * @param out       obiekt klasy PrintWriter
     */
    private static void writeData(Employee[] employees, PrintWriter out) {

        //Zapisuje listę obiektów
        out.println(employees.length);

        for (Employee e : employees) {
            writeEmployee(out, e);
        }
    }

    /**
     * Wczytuje tablicę obiektów klasy Employee
     *
     * @param in obiekt klasy Scanner
     * @return tablica obiektów klasy Employee
     */
    private static Employee[] readData(Scanner in) {
        //Pobiera rozmiar tablicy
        int n = in.nextInt();
        in.nextLine(); //Pobiera znak nowego wiersza
        var employees = new Employee[n];
        for (int i = 0; i < n; i++) {
            employees[i] = readEmployee(in);
        }
        return employees;
    }


    /**
     * Zaousyhe dane obiektu klasy Employee do obiektu plasy PrintWriter
     *
     * @param out obiekt klasy PrintWriter
     * @param e   obiekt klasy Employee
     */
    public static void writeEmployee(PrintWriter out, Employee e) {
        out.println(e.getFullName() + "|" + e.getSalary() + "|" + e.getHireDay());
    }


    /**
     * Wczytuje dane obiektu klasy Employee
     *
     * @param in obiekt klasy Scanner
     * @return obiekt klasy Employee
     */
    public static Employee readEmployee(Scanner in) {
        String line = in.nextLine();
        String[] tokens = line.split("\\|");
        String name = tokens[0];
        double salary = Double.parseDouble(tokens[1]);
        LocalDate hireDate = LocalDate.parse(tokens[2]);
        int year = hireDate.getYear();
        int month = hireDate.getMonthValue();
        int day = hireDate.getDayOfMonth();
        return new Employee(name, salary, year, month, day);
    }
}
