package serialClone;

/**
 * @version 1.22 2018-05-01
 * @author Cay Horstmann
 */
public class SerialCloneTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        var harry = new Employee("Harry Hacker", 35000, 1989, 10,1);
        //Klonuje obiekt harry
        var harry2 = (Employee) harry.clone();

        //Modyfikuje obiekt harry
        harry.raiseSalary(10);

        //Teraz obiekt harry i jego klon różnią się
        System.out.println(harry);
        System.out.println(harry2);
    }
}


