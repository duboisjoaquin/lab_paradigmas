package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;

public class CapitalizedWordHeuristic implements Serializable{

    static public JavaRDD<String> extractCandidates(JavaRDD<String> lines) {
        return lines.flatMap(line -> {
            List<String> candidates = new ArrayList<>();
            String text = line;
            text = text.replaceAll("[-+.^:,\"]", "");
            text = Normalizer.normalize(text, Normalizer.Form.NFD);
            text = text.replaceAll("\\p{M}", "");

            Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");

            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                candidates.add(matcher.group());
            }
            return candidates.iterator();
        });
    }
}
