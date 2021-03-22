package objectStream;

public class Manager extends Employee {
    private Employee secretary;

    /**
     * Konstruuje obiekt menedżera bez sekretarkę/sekretarza
     * @param name imię pracownika
     * @param salary wynagrodzenie
     * @param year rok zatrudnienia
     * @param month miesiąc zatrudnienia
     * @param day dzień zatrudnienia
     */
    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        secretary = null;
    }

    /**
     * Przypisuje sekretarkę/sekretarza menedżerowi
     * @param secretary sekretarka/sekretarz
     */
    public void setSecretary(Employee secretary) {
        this.secretary = secretary;
    }

    @Override
    public String toString() {
        return super.toString() + "[secretary=" + secretary + "]";
    }
}
