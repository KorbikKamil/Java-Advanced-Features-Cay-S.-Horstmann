package parallel;

import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParallelStreams {
    public static void main(String[] args) throws IOException {
        var content = Files.readString(Paths.get("gutenberg/alice30.txt"));
        List<String> wordList = List.of(content.split("\\PL+"));

        //to przykład bardzi złego kodu
        var shortWords = new int[10];
        wordList.parallelStream().forEach(s -> {
            if (s.length() < 10) shortWords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortWords));

        //Kolejna próba: wynik zapewne będzie inny (i również błędny)
        Arrays.fill(shortWords, 0);
        wordList.parallelStream().forEach(s -> {
            if (s.length() < 10) shortWords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortWords));

        //Rozwiązanie problemu: zgrupowanie i zliczenie
        Map<Integer, Long> shortWordCounts = wordList.parallelStream()
                .filter(s -> s.length() < 10)
                .collect(groupingBy(String::length, counting()));
        System.out.println(shortWordCounts);

        //Kolejność grupowania nie jest deterministyczna
        Map<Integer, List<String>> result = wordList.parallelStream().collect(groupingByConcurrent(String::length));

        System.out.println(result.get(14));

        result = wordList.parallelStream().collect(groupingByConcurrent(String::length));

        System.out.println(result.get(14));

        Map<Integer, Long> wordCounts = wordList.parallelStream().collect(groupingByConcurrent(String::length, counting()));

        System.out.println(wordCounts);
    }
}
