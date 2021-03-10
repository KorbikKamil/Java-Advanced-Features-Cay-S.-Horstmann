package optional;

/**
 * @version 1.01 2018-05-01
 * @author Cay Horstmann
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) throws IOException {
        var contents = new String(Files.readAllBytes(
                Paths.get("gutenberg/alice30.txt")), StandardCharsets.UTF_8);
        List<String> wordList = List.of(contents.split("\\PL+"));

        Optional<String> optionalValue = wordList.stream()
                .filter(s -> s.contains("fred"))
                .findFirst();
        System.out.println(optionalValue.orElse("Brak słów") + " zawierających 'fred'");

        Optional<String> optionalString = Optional.empty();
        String result = optionalString.orElse("Brak");
        System.out.println("Wynik: " + result);
        result = optionalString.orElseGet(() -> Locale.getDefault().getDisplayName());
        System.out.println("Wynik: " + result);
        try {
            result = optionalString.orElseThrow(IllegalStateException::new);
            System.out.println("Wynik: " + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        optionalValue = wordList.stream()
                .filter(s -> s.contains("red"))
                .findFirst();
        optionalValue.ifPresent(s -> System.out.println(s + " zawiera 'red'"));

        var results = new HashSet<String>();
        optionalValue.ifPresent(results::add);
        Optional<Boolean> added = optionalValue.map(results::add);
        System.out.println(added);

        System.out.println(inverse(4.0).flatMap(OptionalTest::sqareRoot));
        System.out.println(inverse(-1.0).flatMap(OptionalTest::sqareRoot));
        System.out.println(inverse(0.0).flatMap(OptionalTest::sqareRoot));
        Optional<Double> result2 = Optional.of(-4.0)
                .flatMap(OptionalTest::inverse).flatMap(OptionalTest::sqareRoot);
        System.out.println(result2);
    }

    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    public static Optional<Double> sqareRoot(Double x){
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
