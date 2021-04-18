package localdates;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Cay Horstmann
 * @version 1.0 2016-05-10
 */
public class LocalDates {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now(); //Dzisiejsza data
        System.out.println("today: " + today);

        LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14);
        alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14); //Użycie typu wyliczeniowego Month
        System.out.println("Urodziny Alonza Churcha: " + alonzosBirthday); //Alonzo Church - wynalazca rachunku lambda

        LocalDate programmersDay = LocalDate.of(2021, 1, 1).plusDays(255);
        //13 września, lecz w roku przestępnym będzie to 12 września
        System.out.println("Dzień Programisty: " + programmersDay);

        LocalDate christmas = LocalDate.of(2021, Month.DECEMBER, 25);

        System.out.println("Pozostało do Świąt Bożego Narodzenia: " + today.until(christmas));
        System.out.println("Pozostało dni do Świąt Bożego Narodzenia: " + today.until(christmas, ChronoUnit.DAYS));

        System.out.println(LocalDate.of(2021, 1, 31).plusMonths(1));
        System.out.println(LocalDate.of(2021, 3, 31).minusMonths(1));

        DayOfWeek startOfLastMillenium = LocalDate.of(1900, 1, 1).getDayOfWeek();
        System.out.println("Początek poprzedniego wieku: " + startOfLastMillenium);
        System.out.println(startOfLastMillenium.getValue());
        System.out.println(DayOfWeek.SATURDAY.plus(3));

        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate endExclusive = LocalDate.now();
        Stream<LocalDate> firstDayInMonth = start.datesUntil(endExclusive, Period.ofMonths(1));
        System.out.println("Pierwsze dni miesięcy: " + firstDayInMonth.collect(Collectors.toList()));
    }
}
