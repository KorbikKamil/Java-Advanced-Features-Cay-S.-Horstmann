package objectStream;


import java.io.*;

/**
 * @version 1.11 2018-05-01
 * @author Cay Horstmann
 */
public class ObjectStreamTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        var carl = new Manager("Carl Cracker", 75000, 1987, 12, 15);
        carl.setSecretary(harry);
        var tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
        tony.setSecretary(harry);

        var staff = new Employee[3];

        staff[0] = carl;
        staff[1] = harry;
        staff[2] = tony;

        //zapisuje rekordy wszystkich pracowników w pliku employee.dat
        try (var out = new ObjectOutputStream(new FileOutputStream("2.Wejście_i_wyjście/employeeObjectStream.dat"))) {
            out.writeObject(staff);
        }

        try (var in = new ObjectInputStream(new FileInputStream("2.Wejście_i_wyjście/employeeObjectStream.dat"))) {
            //Wczytuje wszystkie rekordy do nowej tablicy

            var newStaff = (Employee[]) in.readObject();

            //Podnosi wynagordzenie asystenta
            newStaff[1].raiseSalary(10);

            //Wyświetla wszystkie wczytane rekordy
            for (Employee e : newStaff) {
                System.out.println(e);
            }
        }
    }
}
