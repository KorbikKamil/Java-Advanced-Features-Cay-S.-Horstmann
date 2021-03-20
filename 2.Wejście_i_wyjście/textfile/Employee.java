package textfile;

import java.time.LocalDate;

public class Employee {
    private String fullName;
    private double salary;
    private LocalDate hireDay;

    public Employee(String fullName, double salary, int year, int month, int day) {
        this.fullName = fullName;
        this.salary = salary;
        hireDay = LocalDate.of(year, month, day);
    }

    public String getFullName() {
        return fullName;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }
}
