package match;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Program wyświetlający wszystkie adresy URL na stronie WWW poprzez dopasowanie
 * wyrażenia regularnego opisującego znacznik <a href=...> języka HTML.
 * Uruchamianie: java match.HrefMatch adresURL
 *
 * @author Cay Horstmann
 * @version 1.03 2018-03-19
 */
public class HrefMatch {
    public static void main(String[] args) {
        try {
            //Pobiera URL z wiersza poleceń lub używa domyślnego
            String urlString;
            if (args.length > 0) urlString = args[0];
            else urlString = "http://openjdk.java.net/";

            //Wczytuje zawartość strony
            InputStream in = new URL(urlString).openStream();
            var input = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            //Wyszukuje wszystkie wystąpienia wzorca
            var patternString = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            pattern.matcher(input)
                    .results()
                    .map(MatchResult::group)
                    .forEach(System.out::println);
        } catch (IOException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }
}
